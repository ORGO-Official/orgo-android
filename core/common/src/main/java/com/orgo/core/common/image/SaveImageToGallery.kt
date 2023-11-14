package com.orgo.core.common.image

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


fun saveImageToGallery(
    context : Context,
    bitmap : Bitmap,
    onSuccess : (Boolean) -> Unit
) {
    val contentValues = ContentValues()
    val fileName = System.currentTimeMillis().toString() + ".png" // 파일이름 현재시간.png
    contentValues.apply {
        put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/오르GO") // 경로 설정
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.IS_PENDING, 1)
            // is_pending -> 다른 곳에서 이 데이터를 요구하면 무시하라는 의미, 해당 저장소를 독점
        }
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    try{
        if(uri != null){
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "w", null)
            if(parcelFileDescriptor != null) {
                val fos = FileOutputStream(parcelFileDescriptor.fileDescriptor)
                //비트맵을 FileOutputStream로 압축해서 저장
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장소 독점을 해제한다.
                contentResolver.update(uri, contentValues, null, null)
                parcelFileDescriptor.close()
                onSuccess(true)
            }
        }
    } catch(e: FileNotFoundException) {
        e.printStackTrace()
        onSuccess(false)
    } catch (e: IOException) {
        e.printStackTrace()
        onSuccess(false)
    } catch (e: Exception) {
        e.printStackTrace()
        onSuccess(false)
    }
}