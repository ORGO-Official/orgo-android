package com.orgo.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.orgo.android.SavedUserToken
import com.orgo.core.common.di.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserTokenDataStore(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        userTokenSerializer: UserTokenSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<SavedUserToken> =
        tmpFolder.testUserTokenDataStore(
            coroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher),
            userTokenSerializer = userTokenSerializer,
        )
}

fun TemporaryFolder.testUserTokenDataStore(
    coroutineScope : CoroutineScope,
    userTokenSerializer: UserTokenSerializer = UserTokenSerializer()
) = DataStoreFactory.create(
    serializer = userTokenSerializer,
    scope = coroutineScope,
){
    newFile("savedUserToken_test.pb")
}
