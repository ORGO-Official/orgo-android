package com.orgo.feature.mountain_detail.restaurant_webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.orgo.core.common.util.decoder.StringDecoder
import com.orgo.core.common.util.decoder.UriDecoder
import com.orgo.core.ui.component.CenterCircularProgressIndicator
import com.orgo.feature.mountain_detail.mountain_detail.MountainDeatilTopAppBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RestaurantWebView(
    url: String,
    stringDecoder: StringDecoder = UriDecoder(),
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val restaurantUrl = stringDecoder.decodeString(url)
    val backEnable = remember { mutableStateOf(false) }
    var webView: WebView? = null
    val restaurantWebViewClient = remember {
        RestaurantWebViewClient(
            context = context,
            backEnable = backEnable,
        )
    }
    val isLoading by restaurantWebViewClient.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(isLoading){
        Timber.tag("Webview").d("isLoading : ${isLoading}")
    }

    BackHandler(enabled = backEnable.value) {
        webView?.goBack()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MountainDeatilTopAppBar(
                title = "인근 식당 상세정보",
                onBackClicked = onBackClicked
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)){
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        webViewClient = restaurantWebViewClient
                        webChromeClient = RestaurantWebChromeClient(it)

                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.safeBrowsingEnabled = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.setSupportMultipleWindows(true)
                        loadUrl(restaurantUrl)
                        webView = this
                    }
                }, update = {
                    webView = it
                })
            if(isLoading){
                CenterCircularProgressIndicator()
            }
        }
    }

}

private class RestaurantWebViewClient(
    val context: Context,
    val backEnable: MutableState<Boolean>,
) : WebViewClient() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        Timber.tag("WebView").d("onPageStarted : hi")
        if(url != null && url.contains("place.map.kakao.com")){
            Timber.tag("WebView").d("onPageStarted : isLoading true")
            _isLoading.update { true }
        }
        backEnable.value = view!!.canGoBack()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        Timber.tag("WebView").d("onPageFinished : hi")
        if(url != null && url.contains("place.map.kakao.com")){
            _isLoading.update { false }
        }
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        Timber.d("request url : ${request?.url}")
        return request?.url?.let { uri ->
            val url = uri.toString()
            val uriIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            if (url.startsWith("intent:")) {
                handleIntentUrl(url)
            } else if (url.startsWith("market:")) {
                startActivity(context, uriIntent, null)
            } else if (url.startsWith("tel:")) {
                handleTelUrl(url)
            } else if (url.contains("m.search.daum.net")) {
                startActivity(context, uriIntent, null)
            } else {
                view?.loadUrl(url)
            }
            true
        } ?: false
    }

    private fun handleIntentUrl(url: String) {
        val webViewIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
        val existPackage = context.packageManager.getLaunchIntentForPackage(
            webViewIntent.`package`!!
        )

        Timber.d("package : ${webViewIntent.`package`}")
        if (existPackage != null) {
            Timber.d("existPackage is not null")
            startActivity(context, webViewIntent, null)
        } else {
            Timber.d("existPackage is null")
            val marketIntent = Intent(Intent.ACTION_VIEW)
            marketIntent.data =
                Uri.parse("market://details?id=" + webViewIntent.`package`)
            startActivity(context, marketIntent, null)
        }
    }

    private fun handleTelUrl(url: String) {
        if (checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            TedPermission.create()
                .setPermissionListener(CallPhonePermissionListener(context))
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setDeniedMessage("서비스 사용을 위해 전화걸기 권한을 설정해주세요\n[Setting] > [Permissions]")
                .check()
        } else {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse(url)
            startActivity(context, callIntent, null)
        }
    }
}

private class CallPhonePermissionListener(private val context: Context) : PermissionListener {
    override fun onPermissionGranted() {
        Toast.makeText(context, "전화걸기 권한이 설정되었습니다.", Toast.LENGTH_SHORT).show();
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Toast.makeText(context,"전화걸기 권한이 거부되었습니다",Toast.LENGTH_SHORT).show()
        Timber.d("deniedPermissions : $deniedPermissions")
    }
}

private class RestaurantWebChromeClient(
    val context: Context
) : WebChromeClient() {
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        val newWebView = WebView(context)
        val transport = resultMsg?.obj as WebView.WebViewTransport
        transport.webView = newWebView
        resultMsg.sendToTarget()
        return true
    }
}