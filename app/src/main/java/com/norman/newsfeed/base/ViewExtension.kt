package com.norman.newsfeed.base

import android.content.res.Resources
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import kotlinx.coroutines.delay

/**
 * dp and px conversion
 */
// Convert px to dp
val Int.toDpValue: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).dp

/**
 * dp and px conversion
 */
//Convert dp to px
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.toDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).dp

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
fun Modifier.topSystemInsetsPadding() = windowInsetsPadding(
    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
)

val Dp.toPx: Int
    get() = this.value.toInt().toPx

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.preventDoubleClickable(
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
): Modifier = composed {
    var clickable by remember { mutableStateOf(true) }

    LaunchedEffect(clickable) {
        if (!clickable) {
            delay(800)

            clickable = true
        }
    }
    Modifier.combinedClickable(
        interactionSource,
        indication,
        enabled && clickable,
        onClickLabel,
        role,
        onLongClick = onLongClick,
        onClick = {
            clickable = false

            onClick()
        }
    )
}

@Stable
@Composable
fun Modifier.noIndicationClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    Modifier.preventDoubleClickable(
        interactionSource = interactionSource,
        indication = null, enabled, onClickLabel, role, onLongClick, onClick
    )
}

@Composable
fun Modifier.thenIf(
    modifier: Modifier,
    condition: @Composable () -> Boolean,
): Modifier {
    return this.then(
        if (condition())
            modifier
        else
            Modifier
    )
}

fun Modifier.traceLayout(traceName: String): Modifier {
    return this
        .testTag(traceName)
        .layout { measurable, constraints ->
            trace(traceName) {
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(IntOffset.Zero)
                }
            }
        }
}
