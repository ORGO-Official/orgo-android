package com.orgo.core.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun CircularAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    imageSize: Dp,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentDescription: String? = null,
    placeholder : Painter? = null,
    contentScale : ContentScale = ContentScale.Fit
) {
    Box(
        modifier = modifier
            .size(imageSize)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            placeholder = placeholder,
            error = placeholder,
            fallback = placeholder,
            contentScale = contentScale
        )

    }
}

@Composable
fun CircularBitmapImage(
    modifier: Modifier = Modifier,
    bitmap : Bitmap,
    imageSize: Dp,
    colorFilter : ColorFilter? = null,
    contentDescription: String? = null,
    contentScale : ContentScale = ContentScale.Fit
) {
    Box(
        modifier = modifier
            .size(imageSize),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            painter = rememberAsyncImagePainter(bitmap),
            contentDescription = contentDescription,
            contentScale = contentScale,
            colorFilter = colorFilter
        )
    }
}