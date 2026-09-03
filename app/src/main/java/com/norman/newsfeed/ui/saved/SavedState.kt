package com.norman.newsfeed.ui.saved

import com.norman.newsfeed.base.UiState

data class SavedState(
    val temp: String
): UiState {
    companion object {
        val initial = SavedState(
            temp = ""
        )
    }
}
