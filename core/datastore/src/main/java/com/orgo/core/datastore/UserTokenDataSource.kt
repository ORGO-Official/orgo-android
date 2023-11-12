package com.orgo.core.datastore

import androidx.datastore.core.DataStore
import com.orgo.android.SavedUserToken
import com.orgo.core.model.data.SocialType
import com.orgo.core.model.data.UserToken
import com.orgo.core.model.data.mapSocialType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class UserTokenDataSource @Inject constructor (
    private val dataStore: DataStore<SavedUserToken>
){
    /**
     * Proto DataStore에 UserToken을 저장함
     */
    suspend fun updateUserToken(socialType: SocialType, accessToken: String, refreshToken: String) {
        Timber.tag("datastore").d("saveUserTokenToDataStore 들어옴")
        try{
            dataStore.updateData { userToken ->
                userToken.toBuilder()
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setSocialType(socialType.value)
                    .build()
            }
        }catch (ioException : IOException){
            Timber.e("Failed to update user token")
        }
        Timber.tag("datastore").d("saveUserTokenToDataStore 끝남")
    }

    suspend fun refreshUserToken(accessToken: String){
        try{
            dataStore.updateData { userToken ->
                userToken.toBuilder()
                    .setAccessToken(accessToken)
                    .build()
            }
        }catch (ioException : IOException){
            Timber.e("Failed to refresh user token")
        }
    }

    /**
     * Proto DataStore에 있는 UserToken을 DefaultInstance로 초기화함
     */
    suspend fun removeUserToken(){
        try{
            dataStore.updateData {
                SavedUserToken.getDefaultInstance()
            }
        }catch(ioException : IOException){
            Timber.e("Failed to init user token")
        }
    }
    /**
     * Proto DataStore에서 AccessToken을 가져옴 / exception catch 수행
     */
    suspend fun getUserAccessToken() : String {
        return dataStore.data
            .catch { exception ->
                Timber.e("getUserAccessToken exception.message : ${exception.message}")
            }.first()
            .accessToken
    }

    suspend fun getUserRefreshToken() : String {
        return dataStore.data
            .catch { exception ->
                Timber.e("getUserRefreshToken exception.message : ${exception.message}")
            }.first()
            .refreshToken
    }

    suspend fun getSocialType() : SocialType{
        return dataStore.data
            .catch { exception ->
                Timber.e("getUserToken exception.message : ${exception.message}")
            }.map {
                Timber.d("savedUserToken : access : ${it.accessToken}\n refresh : ${it.refreshToken} \n social type : ${it.socialType}")
                mapSocialType(it.socialType)
            }.first()
    }
}