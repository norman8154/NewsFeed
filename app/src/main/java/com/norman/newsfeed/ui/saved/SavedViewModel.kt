package com.norman.newsfeed.ui.saved

import androidx.lifecycle.viewModelScope
import com.norman.newsfeed.base.BaseViewModel
import com.norman.newsfeed.useCase.ArticleUseCase
import com.norman.repository.articleRepository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.mutate
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

                _uiState.update { state ->
                    state.copy(
                        articleList = state.articleList.mutate { list ->
                            val index = list.indexOfFirst { it.id == intent.articleBO.id }

                            if (index >= 0) {
                                list[index] = list[index].copy(isSaved = !intent.articleBO.isSaved)
                            }
                        }
                    )
                }
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

                    _uiState.update { state ->
                        val currentIdSet = state.articleList.map { it.id }.toSet()
                        val addedArticleList = articleList.filterNot { it.id in currentIdSet }

                        state.copy(articleList = addedArticleList.toPersistentList().addingAll(state.articleList))
                    }
                }
        }
    }

}
