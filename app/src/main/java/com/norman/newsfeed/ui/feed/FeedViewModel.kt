package com.norman.newsfeed.ui.feed

import androidx.lifecycle.viewModelScope
import com.norman.newsfeed.base.BaseViewModel
import com.norman.newsfeed.pojo.FeedListItem
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
) : BaseViewModel<FeedState, FeedIntent, FeedEvent>(FeedState.initial) {

    companion object {
        private const val ARTICLE_PAGE_SIZE = 20
    }

    private var isDataLoaded = false

    init {
        observeSavedArticleList()
    }

    override suspend fun handleIntent(intent: FeedIntent) {
        when (intent) {

            is FeedIntent.Init -> {
                if (!isDataLoaded) {
                    isDataLoaded = true

                    _uiState.update {
                        it.copy(isArticleFetching = true)
                    }

                    fetchArticleList()
                }
            }

            is FeedIntent.OnArticleLoadMore -> {
                if (!uiState.value.isArticleFetching) {
                    _uiState.update {
                        it.copy(isArticleFetching = true)
                    }

                    fetchArticleList(offset = uiState.value.feedList.count { it is FeedListItem.Article })
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

            is FeedIntent.OnUserClickSaveArticle -> {
                viewModelScope.launch {
                    if (intent.articleBO.isSaved) {
                        articleRepository.deleteSavedArticleById(intent.articleBO.id)
                    } else {
                        articleUseCase.saveArticleBOToDB(intent.articleBO)
                    }
                }
            }

            is FeedIntent.OnUserClickArticle -> {
                emitUiEvent(FeedEvent.OnNavigateToArticleDetail(intent.articleBO.id))
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
                    ).map { FeedListItem.Article(it) }

                    _uiState.update {
                        it.copy(
                            feedList = if (isRefreshing) {
                                articleList.toPersistentList()
                            } else {
                                it.feedList.addingAll(articleList)
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

    private fun observeSavedArticleList() {
        viewModelScope.launch {
            articleRepository.getAllSavedArticleFlow()
                .collect { savedArticleList ->
                    val savedIdSet = savedArticleList.map { it.id }.toSet()

                    _uiState.update {
                        it.copy(
                            feedList = it.feedList.map { item ->
                                if (item is FeedListItem.Article) {
                                    item.copy(
                                        articleBO = item.articleBO.copy(
                                            isSaved = savedIdSet.contains(
                                                item.articleBO.id
                                            )
                                        )
                                    )
                                } else {
                                    item
                                }
                            }.toPersistentList()
                        )
                    }
                }
        }
    }
}
