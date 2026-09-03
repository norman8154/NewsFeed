package com.norman.newsfeed.composable

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest

@Stable
@Composable
fun BasicImage(
    modifier: Modifier,
    imageUrl: String?,
    @DrawableRes placeHolder: Int? = null,
    @DrawableRes error: Int? = null,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
    alignment: Alignment = Alignment.Center,
    useDiskCache: Boolean = true,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
            memoryCacheKey(imageUrl)
            networkCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)

            if (useDiskCache) {
                diskCacheKey(imageUrl)
                diskCachePolicy(CachePolicy.ENABLED)
            }
        }.build(),
        filterQuality = FilterQuality.Medium,
        onSuccess = {
            onSuccess?.invoke(it)
        },
        onLoading = onLoading,
        onError = onError,
        contentDescription = null,
        alignment = alignment,
        colorFilter = colorFilter,
        contentScale = contentScale,
        modifier = modifier,
        error = if (error != null) rememberAsyncImagePainter(error) else null,
        placeholder = if (placeHolder != null) rememberAsyncImagePainter(placeHolder) else null
    )
}