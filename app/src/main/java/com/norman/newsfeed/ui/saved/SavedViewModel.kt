package com.norman.newsfeed.ui.saved

import androidx.lifecycle.viewModelScope
import com.norman.newsfeed.base.BaseViewModel
import com.norman.newsfeed.useCase.ArticleUseCase
import com.norman.repository.articleRepository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val articleUseCase: ArticleUseCase
): BaseViewModel<SavedState, SavedIntent, SavedEvent>(SavedState.initial) {

    init {
        observeSavedArticleList()
    }

    override suspend fun handleIntent(intent: SavedIntent) {
        when (intent) {

            is SavedIntent.OnUserClickSaveArticle -> {
                viewModelScope.launch {
                    if (intent.articleBO.isSaved) {
                        articleRepository.deleteSavedArticleById(intent.articleBO.id)
                    } else {
                        articleUseCase.saveArticleBOToDB(intent.articleBO)
                    }
                }
            }

            is SavedIntent.OnUserClickArticle -> {
                emitUiEvent(SavedEvent.OnNavigateToArticleDetail(intent.articleBO.id))
            }
        }
    }

    private fun observeSavedArticleList() {
        viewModelScope.launch {
            articleRepository.getAllSavedArticleFlow()
                .collect { savedArticleList ->
                    val articleList = articleUseCase.convertSavedArticleEntityToArticleBO(
                        savedArticleList = savedArticleList,
                    )
                    val savedIdSet = savedArticleList.map { it.id }.toSet()

                    _uiState.update { state ->
                        val currentIdSet = state.articleList.map { it.id }.toSet()
                        val addedArticleList = articleList.filterNot { it.id in currentIdSet }
                        val currentArticleList = state.articleList.map {
                            it.copy(isSaved = it.id in savedIdSet)
                        }

                        state.copy(
                            articleList = addedArticleList.toPersistentList()
                                .addingAll(currentArticleList)
                        )
                    }
                }
        }
    }

}
