package com.orgo.core.datastore

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

class UserTokenDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject : UserTokenDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup(){
        subject = UserTokenDataSource(
            tmpFolder.testUserTokenDataStore(testScope)
        )
    }

    @Test
    fun test_refresh_accessToken()  = testScope.runTest {
        val testAccessToken = "1231414"
        subject.refreshUserToken(testAccessToken)
        assertEquals(
            testAccessToken,
            subject.getUserAccessToken()
        )
//        assertEquals(testAccessToken,testAccessToken)
    }
}
