package com.orgo.core.common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

// Bitmap을 문자열로 변환하는 함수
fun bitmapToString(bitmap: Bitmap?): String? {
    try{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

// 문자열을 Bitmap으로 변환하는 함수
fun stringToBitmap(encodedString: String?): Bitmap? {
    try {
        val decodedByteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}