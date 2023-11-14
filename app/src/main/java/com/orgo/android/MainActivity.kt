package com.orgo.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kakao.sdk.common.util.Utility
import com.orgo.android.ui.OrgoApp
import com.orgo.core.designsystem.theme.ORGOTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
//        splashScreen.setKeepOnScreenCondition { true }
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        window.apply { statusBarColor = Color.WHITE }


//        val keyHash = Utility.getKeyHash(this)
//        Timber.d("keyHash : $keyHash")
        setContent {
            ORGOTheme {
                OrgoApp()
            }
        }
    }
}
