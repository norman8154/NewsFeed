package com.norman.newsfeed.composable

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState 尚未提供，請確認畫面位於 MainScreen 之內")
}
