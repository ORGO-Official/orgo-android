package com.orgo.core.data.repositoryImpl

import com.orgo.core.common.auth.KakaoAuthManager
import com.orgo.core.common.auth.NaverAuthManager
import com.orgo.core.common.di.IoDispatcher
import com.orgo.core.network.util.apiResultToResourceFlow
import com.orgo.core.data.repository.UserAuthRepository
import com.orgo.core.data.repository.UserRepository
import com.orgo.core.datastore.UserTokenDataSource
import com.orgo.core.model.data.UserAuthState
import com.orgo.core.model.data.SocialType
import com.orgo.core.model.data.UserToken
import com.orgo.core.model.data.mapSocialType
import com.orgo.core.model.network.ApiResult
import com.orgo.core.model.network.Resource
import com.orgo.core.model.network.onErrorOrException
import com.orgo.core.model.network.onSuccess
import com.orgo.core.network.datasource.UserAuthRemoteDataSource
import com.orgo.core.network.datasource.UserRemoteDataSource
import com.orgo.core.network.util.apiResultToResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.lang.IllegalArgumentException
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userAuthRemoteDataSource: UserAuthRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userTokenDataSource: UserTokenDataSource,
    private val kakaoAuthManager: KakaoAuthManager,
    private val naverAuthManager: NaverAuthManager
) : UserAuthRepository {

    private val _userAuthState = MutableStateFlow<UserAuthState>(UserAuthState.Unauthenticated)
    override val userAuthState: StateFlow<UserAuthState> = _userAuthState.asStateFlow()

    override suspend fun executeLogin(
        socialType: SocialType,
        token: String
    ): Flow<Resource<String>> = flow {
        userAuthRemoteDataSource.getUserToken(socialType, token)
            .onSuccess { userToken ->
                _userAuthState.update {
                    UserAuthState.Authenticated(
                        userToken = userToken,
                        loginSocialType = socialType,
                    )
                }
                saveUserToken(socialType, userToken)
                emit(Resource.Success("Success"))
            }.onErrorOrException { code, message ->
                emit(
                    Resource.Failure(
                        code = code,
                        errorMessage = message ?: "UnknownError"
                    )
                )
            }
    }

    override suspend fun executeAutoLogin(): Flow<Resource<String>> = flow {
//        userTokenDataSource.removeUserToken()
        // userProfile을 조회하여 토큰 유효성 검사
        userAuthActionFlow { userAccessToken ->
            userRemoteDataSource.getUserProfile(userAccessToken)
        }.collect { resource ->
            Timber.tag("hotfix").d("resource : $resource")
            when (resource) {
                is Resource.Loading -> emit(Resource.Loading)
                is Resource.Success -> {
                    if (resource.data.id == 0L) {
                        emit(Resource.Failure("Token Expired", 401))
                    } else {
                        _userAuthState.update {
                            UserAuthState.Authenticated(
                                userToken = UserToken(
                                    accessToken = userTokenDataSource.getUserAccessToken(),
                                    refreshToken = userTokenDataSource.getUserRefreshToken()
                                ),
                                loginSocialType = userTokenDataSource.getSocialType()
                            )
                        }
                        emit(Resource.Success("Success"))
                    }
                }

                is Resource.Failure -> {
                    emit(resource)
                }
            }
        }
    }

    override suspend fun executeLogout(): Flow<Resource<String>> {
        val userAccessToken = userTokenDataSource.getUserAccessToken()
        val socialToken = try {
            getSocialToken()
        }catch (e : Exception){
            return flow {
                emit(Resource.Failure(code = 400, errorMessage = e.message ?: "Unknown Error"))
            }
        }
        return apiResultToResourceFlow {
            userAuthRemoteDataSource.executeLogout(
                socialToken = socialToken,
                userAccessToken = userAccessToken
            ).onSuccess {
                _userAuthState.update { UserAuthState.LoggedOut }
                executeSocialLogout()
                userTokenDataSource.removeUserToken()
            }
        }
    }
    override suspend fun executeWithdraw(): Flow<Resource<String>> {
        val userAccessToken = userTokenDataSource.getUserAccessToken()
        val socialToken = try {
            getSocialToken()
        }catch (e : Exception){
            return flow {
                emit(Resource.Failure(code = 400, errorMessage = e.message ?: "Unknown Error"))
            }
        }

        return apiResultToResourceFlow {
            userAuthRemoteDataSource.executeWithdraw(
                socialToken,
                userAccessToken
            ).onSuccess {
                _userAuthState.update { UserAuthState.LoggedOut }
                executeSocialWithdraw()
                userTokenDataSource.removeUserToken()
            }
        }
    }

    override suspend fun getSocialType(): SocialType {
        return userTokenDataSource.getSocialType()
    }

    // UseAccessToken이 있는지 확인하는 로직
    // Refresh 실패할 경우 Resource.Failure emit
    override fun <T : Any> userAuthActionFlow(
        apiCall: suspend (String) -> ApiResult<T>
    ): Flow<Resource<T>> = flow {
        Timber.d("userAuthActionFlow 들어옴")
//        userTokenDataStore.removeUserToken()
        val userAccessToken = userTokenDataSource.getUserAccessToken()
        if (userAccessToken.isEmpty()) {
            emit(Resource.Failure("UserAccessToken is Empty", 401))
        } else {
            when (val actionResult = apiResultToResource { apiCall(userAccessToken) }) {
                is Resource.Loading -> Unit
                is Resource.Success -> emit(actionResult)
                is Resource.Failure -> {
                    // 토큰 만료 되었을 경우 Refresh
                    when (actionResult.code) {
                        401 -> {
                            val reissueResult =
                                reissueAccessToken(userTokenDataSource.getUserAccessToken())
                            when (reissueResult) {
                                is Resource.Success -> {
                                    val newAccessToken = reissueResult.data
                                    userTokenDataSource.refreshUserToken(newAccessToken)
                                    emit(apiResultToResource { apiCall(newAccessToken) })
                                }

                                is Resource.Failure -> emit(reissueResult)
                                is Resource.Loading -> Unit
                            }
                        }
                        else -> emit(actionResult)
                    }
                }
            }
        }
    }.flowOn(ioDispatcher)

    private suspend fun saveUserToken(socialType: SocialType, userToken: UserToken) {
        userTokenDataSource.updateUserToken(
            socialType,
            userToken.accessToken,
            userToken.refreshToken
        )
    }

    private suspend fun reissueAccessToken(
        userAccessToken: String,
    ): Resource<String> {
        val userRefreshToken = userTokenDataSource.getUserRefreshToken()
        val refreshResult = apiResultToResource {
            userAuthRemoteDataSource.executeReissue(userAccessToken, userRefreshToken)
        }
        return when (refreshResult) {
            is Resource.Success -> refreshResult
            is Resource.Failure -> {
                when (refreshResult.code) {
                    401 -> Resource.Failure("RefreshToken is expired", 401)
                    else -> refreshResult
                }
            }

            is Resource.Loading -> refreshResult
        }
    }


    private suspend fun executeSocialLogout() {
        val socialType = getSocialType()
        // 카카오 로그인은 백엔드에서 로그아웃 처리
        if (socialType == SocialType.NAVER) {
            naverAuthManager.executeLogout()
        }
    }

    private suspend fun executeSocialWithdraw() {
        val socialType = getSocialType()
        if (socialType == SocialType.NAVER) {
            naverAuthManager.executeWithdraw()
        }
    }
    private suspend fun getSocialToken(): String {
        return when (userTokenDataSource.getSocialType()) {
            SocialType.KAKAO -> {
                kakaoAuthManager.getAccessToken()
                    ?: throw NullPointerException("KakaoAccessToken is null")
            }
            SocialType.NAVER -> {
                naverAuthManager.getAccessTokenWithRefresh()
            }
            else -> throw IllegalAccessException("socialType error")
        }
    }
}

