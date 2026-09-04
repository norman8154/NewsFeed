package com.norman.newsfeed.ui.detail

import androidx.lifecycle.viewModelScope
import com.norman.newsfeed.base.BaseViewModel
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.newsfeed.pojo.ToastType
import com.norman.newsfeed.useCase.ArticleUseCase
import com.norman.repository.articleRepository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val articleUseCase: ArticleUseCase
) : BaseViewModel<ArticleDetailState, ArticleDetailIntent, ArticleDetailEvent>(ArticleDetailState.initial) {

    private var isDataLoaded = false

    init {
        observeSavedArticleIdList()
    }

    override suspend fun handleIntent(intent: ArticleDetailIntent) {
        when (intent) {

            is ArticleDetailIntent.Init -> {
                if (!isDataLoaded) {
                    isDataLoaded = true

                    fetchArticle(intent.articleId)
                }
            }

            is ArticleDetailIntent.OnUserClickSaveArticle -> {
                viewModelScope.launch {
                    if (intent.articleBO.isSaved) {
                        articleRepository.deleteSavedArticleById(intent.articleBO.id)

                        emitUiEvent(ArticleDetailEvent.OnShowToast(ToastType.UnSaved))
                    } else {
                        articleUseCase.saveArticleBOToDB(intent.articleBO)

                        emitUiEvent(ArticleDetailEvent.OnShowToast(ToastType.Saved))
                    }
                }
            }
        }
    }

    private fun fetchArticle(articleId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isArticleFetching = true)
            }

            val articleBO = articleRepository.fetchArticle(articleId)
                .getOrNull()
                ?.let { response ->
                    articleUseCase.convertArticleResponseToArticleBO(
                        article = response,
                        isSaved = articleRepository.getSavedArticleById(articleId) != null,
                    )
                }
                ?: getArticleFromDB(articleId)

            if (articleBO == null) {
                emitUiEvent(ArticleDetailEvent.OnShowToast(ToastType.Unknown))
            }

            _uiState.update {
                it.copy(
                    articleBO = articleBO,
                    isArticleFetching = false,
                )
            }
        }
    }

    private suspend fun getArticleFromDB(articleId: Long): ArticleBO? {
        articleRepository.getSavedArticleById(articleId)?.let { savedArticle ->
            return articleUseCase.convertSavedArticleEntityToArticleBO(savedArticle = savedArticle)
        }

        return articleRepository.getCachedArticleById(articleId)?.let { cachedArticle ->
            articleUseCase.convertCachedArticleEntityToArticleBO(
                cachedArticle = cachedArticle,
                isSaved = false,
            )
        }
    }

    private fun observeSavedArticleIdList() {
        viewModelScope.launch {
            articleRepository.getSavedArticleIdListFlow()
                .collect { savedIdList ->
                    val savedIdSet = savedIdList.toSet()

                    _uiState.update { state ->
                        val articleBO = state.articleBO ?: return@update state

                        state.copy(articleBO = articleBO.copy(isSaved = articleBO.id in savedIdSet))
                    }
                }
        }
    }
}
