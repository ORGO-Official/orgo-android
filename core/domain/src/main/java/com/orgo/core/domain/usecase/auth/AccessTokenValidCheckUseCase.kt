package com.orgo.core.domain.usecase.auth

import com.orgo.core.data.repository.UserRepository
import com.orgo.core.model.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class AccessTokenValidCheckUseCase @Inject constructor(
    private val userRepository: UserRepository,
){
    operator fun invoke() : Flow<Resource<String>> = flow {
        // userAccessToken이 달린 다른 API 호출해봄으로써 토큰이 유효한가 검사하기
       userRepository.getUserProfile().collect{ resource ->
           Timber.tag("hotfix").d("resource : $resource")
           when(resource){
               is Resource.Loading -> emit(Resource.Loading)
               is Resource.Success -> {
                   if(resource.data.id == 0L){
                       emit(Resource.Failure("Token Expired", 401))
                   }else emit(Resource.Success("Success"))
               }
               is Resource.Failure -> {
                   emit(resource)
               }
           }
       }
    }
}