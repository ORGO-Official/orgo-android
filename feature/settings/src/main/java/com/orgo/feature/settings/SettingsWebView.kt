package com.orgo.feature.settings

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView


@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsWebView(
    text : String,
    url: String,
    onBackClicked : () -> Unit,
) {
    val backEnable = remember { mutableStateOf(false) }
    var webView: WebView? = null

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                text = text,
                onBackClicked = onBackClicked
            )
        }
    ) { padding ->
        AndroidView(
            modifier = Modifier.padding(padding),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = SettingsWebView(it,backEnable)
                    webChromeClient = WebChromeClient()
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                    settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
                    loadUrl(url)
                    webView = this
                }
            },
            update = {
                webView = it
            }
        )
        BackHandler(enabled = backEnable.value) {
            webView?.goBack()
        }
    }
}


private class SettingsWebView(
    val context: Context,
    val backEnable: MutableState<Boolean>
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        backEnable.value = view!!.canGoBack()
    }
}