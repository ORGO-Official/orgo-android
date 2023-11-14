package com.orgo.core.common.image

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber


@Composable
fun GetImageFromGallery(
    shouldExecute : Boolean,
    onSuccess : (Bitmap?) -> Unit,
) {
    var imageUri by remember {  mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let{
            Timber.tag("bitmap").d("uri  $it")
            if(Build.VERSION.SDK_INT < 28){
                onSuccess(MediaStore.Images.Media.getBitmap(context.contentResolver,it))
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                onSuccess(ImageDecoder.decodeBitmap(
                    source,
                    ImageDecoder.OnHeaderDecodedListener{ decoder, info, source ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }
                ))
            }
        }
    }

    LaunchedEffect(shouldExecute){
        if(shouldExecute){
            Timber.tag("bitmap").d("LaunchedEffect")
            launcher.launch("image/*")
        }
    }
}


