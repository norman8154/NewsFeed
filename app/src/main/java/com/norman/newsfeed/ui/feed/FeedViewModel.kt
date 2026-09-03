package com.norman.newsfeed.ui.feed

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
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val articleUseCase: ArticleUseCase
): BaseViewModel<FeedState, FeedIntent, FeedEvent>(FeedState.initial) {

    private val ARTICLE_PAGE_SIZE = 20

    override suspend fun handleIntent(intent: FeedIntent) {
        when (intent) {

            is FeedIntent.Init -> {
                _uiState.update {
                    it.copy(isArticleFetching = true)
                }

                fetchArticleList()
            }

            is FeedIntent.OnArticleLoadMore -> {
                if (!uiState.value.isArticleFetching) {
                    _uiState.update {
                        it.copy(isArticleFetching = true)
                    }

                    fetchArticleList(offset = uiState.value.articleList.size)
                }
            }

            is FeedIntent.OnRefreshing -> {
                _uiState.update {
                    it.copy(
                        isArticleFetching = true,
                        isArticleHasMore = true,
                        isArticleRefreshing = true
                    )
                }

                fetchArticleList(offset = 0, isRefreshing = true)
            }
        }
    }

    private fun fetchArticleList(
        limit: Int = ARTICLE_PAGE_SIZE,
        offset: Int = 0,
        isRefreshing: Boolean = false,
    ) {
        viewModelScope.launch {
            articleRepository.fetchArticleList(limit, offset)
                .onSuccess { response ->
                    val savedIdList = articleRepository.getSavedArticleIdList()
                    val articleList = articleUseCase.convertArticleListResponseToArticleBO(
                        response = response,
                        savedIdList = savedIdList,
                    )

                    _uiState.update {
                        it.copy(
                            articleList = if (isRefreshing) {
                                articleList.toPersistentList()
                            } else {
                                it.articleList.addingAll(articleList)
                            },
                            isArticleHasMore = response.next != null,
                            isArticleFetching = false,
                            isArticleRefreshing = false,
                        )
                    }
                }.onFailure {
                    it.printStackTrace()

                    _uiState.update {
                        it.copy(
                            isArticleFetching = false,
                            isArticleRefreshing = false,
                        )
                    }
                }
        }
    }
}
