package com.orgo.core.common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

suspend fun downloadBitmapFromUrl(imageUrl: String?): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.doInput = true // 입력스트림 사용여부
        connection.connect()

        val inputStream = connection.inputStream // getInputStream() : InputStream을 반환해준다.

        BitmapFactory.decodeStream(inputStream) // Bitmap으로 변환

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}