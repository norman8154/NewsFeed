package com.norman.newsfeed.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<uiState : UiState, uiIntent : UiIntent, uiEvent : UiEvent>(
    initialState: uiState,
) : ViewModel() {

    protected val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<uiState> = _uiState.asStateFlow()

    protected val _uiEvent = Channel<uiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<uiEvent> = _uiEvent.receiveAsFlow()

    private val _uiIntent: Channel<uiIntent> = Channel()
    private val uiIntent: Flow<uiIntent> = _uiIntent.receiveAsFlow()

    init {
        viewModelScope.launch {
            uiIntent.collect {
                handleIntent(it)
            }
        }
    }

    abstract suspend fun handleIntent(intent: uiIntent)

    fun sendIntent(intent: uiIntent) {
        viewModelScope.launch {
            _uiIntent.send(intent)
        }
    }

    protected suspend fun emitUiEvent(event: uiEvent) {
        _uiEvent.send(event)
    }
}