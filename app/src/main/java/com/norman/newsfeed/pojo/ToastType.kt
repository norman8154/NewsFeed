package com.norman.newsfeed.pojo

import androidx.annotation.StringRes
import com.norman.resource.R

sealed class ToastType {

    data object Unknown : ToastType()

    data object Saved : ToastType()

    data object UnSaved : ToastType()

}

@get:StringRes
val ToastType.messageResId: Int
    get() = when (this) {
        ToastType.Saved -> R.string.toast_saved

        ToastType.UnSaved -> R.string.toast_un_saved

        ToastType.Unknown -> R.string.toast_unknown
    }
