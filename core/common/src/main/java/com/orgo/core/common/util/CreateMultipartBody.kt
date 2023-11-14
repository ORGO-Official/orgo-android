package com.orgo.core.common.util

import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

// 비트맵을 바이트 배열로 변환합니다.
fun createMultipartBody(bitmap: Bitmap, fileName: String): MultipartBody.Part {
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
    val bitmapData = bos.toByteArray()

    // 바이트 배열을 임시 파일로 저장합니다.
    val file = File.createTempFile("image", "jpeg")
    val fos = FileOutputStream(file)
    fos.write(bitmapData)
    fos.flush()
    fos.close()

    // 임시 파일을 MultipartBody.Part 객체로 변환합니다.
    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("imageFile", fileName, requestFile)
}