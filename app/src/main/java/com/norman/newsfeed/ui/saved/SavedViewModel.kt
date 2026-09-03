package com.norman.newsfeed.ui.saved

import com.norman.newsfeed.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(

): BaseViewModel<SavedState, SavedIntent, SavedEvent>(SavedState.initial) {

    override suspend fun handleIntent(intent: SavedIntent) {

    }

}
