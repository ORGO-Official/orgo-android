package com.orgo.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import com.orgo.android.SavedUserToken
import com.orgo.core.common.di.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "savedUserToken.pb"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserTokenDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        userTokenSerializer: UserTokenSerializer
    ): DataStore<SavedUserToken> =
        DataStoreFactory.create(
            serializer = userTokenSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        ) {
            context.dataStoreFile(DATA_STORE_FILE_NAME)
        }

//    @Provides
//    @Singleton
//    fun provideUserTokenDataSource(
//        dataStore: DataStore<SavedUserToken>
//    ) : UserTokenDataSource{
//        return UserTokenDataSource(dataStore)
//    }
}

//    {
//        return UserTokenDataStore(context.userTokenStore)
////    }
//private val Context.userTokenStore : DataStore<SavedUserToken> by dataStore(
//    fileName = DATA_STORE_FILE_NAME,
//    serializer = UserTokenSerializer()
//)