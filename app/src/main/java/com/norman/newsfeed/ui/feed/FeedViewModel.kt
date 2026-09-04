package com.norman.newsfeed.ui.feed

import androidx.lifecycle.viewModelScope
import com.norman.newsfeed.base.BaseViewModel
import com.norman.newsfeed.pojo.ArticleBO
import com.norman.newsfeed.pojo.FeedListItem
import com.norman.newsfeed.pojo.ServiceCardBO
import com.norman.newsfeed.pojo.ToastType
import com.norman.newsfeed.pojo.WeatherBO
import com.norman.newsfeed.useCase.ArticleUseCase
import com.norman.newsfeed.useCase.ServiceCardUseCase
import com.norman.newsfeed.useCase.WeatherUseCase
import com.norman.repository.articleRepository.ArticleRepository
import com.norman.repository.freshnessRepository.FeedSource
import com.norman.repository.freshnessRepository.FreshnessRepository
import com.norman.repository.networkRepository.NetworkRepository
import com.norman.repository.serviceCardRepository.ServiceCardRepository
import com.norman.repository.weatherRepository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val weatherRepository: WeatherRepository,
    private val serviceCardRepository: ServiceCardRepository,
    private val articleUseCase: ArticleUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val serviceCardUseCase: ServiceCardUseCase,
    private val freshnessRepository: FreshnessRepository,
    private val networkRepository: NetworkRepository,
) : BaseViewModel<FeedState, FeedIntent, FeedEvent>(FeedState.initial) {

    companion object {
        private const val ARTICLE_PAGE_SIZE = 20
        private const val SERVICE_CARD_PAGE_SIZE = 5
        private const val SERVICE_CARD_INDEX = 6

        private const val WEATHER_LATITUDE = 25.03f
        private const val WEATHER_LONGITUDE = 121.56f
    }

    private var isDataLoaded = false
    private var articleList = persistentListOf<ArticleBO>()
    private var articleHasMore = true
    private var weatherBO: WeatherBO? = null
    private var serviceCardList = persistentListOf<ServiceCardBO>()

    init {
        observeSavedArticleList()
        observeNetworkState()
    }

    override suspend fun handleIntent(intent: FeedIntent) {
        when (intent) {

            is FeedIntent.OnEnterPage -> {
                val isInitialLoad = !isDataLoaded

                if (isInitialLoad) {
                    isDataLoaded = true

                    _uiState.update {
                        it.copy(isArticleFetching = true)
                    }
                }

                reloadStaleSources(isInitialLoad = isInitialLoad)
            }

            is FeedIntent.OnArticleLoadMore -> {
                if (!uiState.value.isArticleFetching && !uiState.value.isLowInternet) {
                    _uiState.update {
                        it.copy(isArticleFetching = true)
                    }

                    viewModelScope.launch {
                        loadArticleList(offset = articleList.size)

                        publishFeedList(isLoadFinished = true)
                    }
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

                loadAllSources()
            }

            is FeedIntent.OnUserClickSaveArticle -> {
                viewModelScope.launch {
                    if (intent.articleBO.isSaved) {
                        articleRepository.deleteSavedArticleById(intent.articleBO.id)

                        emitUiEvent(FeedEvent.OnShowToast(ToastType.UnSaved))
                    } else {
                        articleUseCase.saveArticleBOToDB(intent.articleBO)

                        emitUiEvent(FeedEvent.OnShowToast(ToastType.Saved))
                    }
                }
            }

            is FeedIntent.OnUserClickArticle -> {
                emitUiEvent(FeedEvent.OnNavigateToArticleDetail(intent.articleBO.id))
            }
        }
    }

    private fun loadAllSources() {
        viewModelScope.launch {
            coroutineScope {
                val articleDeferred = async {
                    loadFirstPageArticleList()
                }
                val weatherDeferred = async { loadWeather() }
                val serviceCardDeferred = async { loadServiceCardList() }

                awaitAll(articleDeferred, weatherDeferred, serviceCardDeferred)
            }

            publishFeedList(isLoadFinished = true)
        }
    }

    private suspend fun loadFirstPageArticleList() {
        loadArticleList(offset = 0, replaceExisting = true)
    }

    private suspend fun loadArticleList(
        limit: Int = ARTICLE_PAGE_SIZE,
        offset: Int = 0,
        replaceExisting: Boolean = false,
    ) {
        articleRepository.fetchArticleList(limit, offset)
            .onSuccess { response ->
                val savedIdList = articleRepository.getSavedArticleIdList()
                val fetchedArticleList = articleUseCase.convertArticleListResponseToArticleBO(
                    response = response,
                    savedIdList = savedIdList,
                )

                articleList = if (replaceExisting) {
                    fetchedArticleList.toPersistentList()
                } else {
                    articleList.addingAll(fetchedArticleList)
                }

                articleHasMore = response.next != null

                if (offset == 0) {
                    articleUseCase.replaceCachedArticleList(
                        fetchedArticleList.take(ARTICLE_PAGE_SIZE)
                    )

                    freshnessRepository.setLastFetchedAt(FeedSource.ARTICLE)
                }
            }.onFailure {
                it.printStackTrace()

                emitUiEvent(FeedEvent.OnShowToast(ToastType.Unknown))

                if (offset == 0 && articleList.isEmpty()) {
                    val cachedArticleList = articleUseCase.getCachedArticleBOList()

                    if (cachedArticleList.isNotEmpty()) {
                        articleList = cachedArticleList.toPersistentList()
                        articleHasMore = false
                    }
                }
            }
    }

    private fun reloadStaleSources(isInitialLoad: Boolean = false) {
        viewModelScope.launch {
            val isArticleStale = isSourceStale(FeedSource.ARTICLE)
            val shouldReloadArticle = isInitialLoad || isArticleStale || articleList.isEmpty()
            val shouldReloadWeather = isSourceStale(FeedSource.WEATHER) || weatherBO == null
            val shouldReloadServiceCard =
                isSourceStale(FeedSource.SERVICE_CARD) || serviceCardList.isEmpty()

            if (shouldReloadArticle) {
                _uiState.update {
                    it.copy(
                        isArticleFetching = true,
                        isArticleRefreshing = true
                    )
                }
            }

            coroutineScope {
                val articleDeferred = async {
                    if (shouldReloadArticle) {
                        loadFirstPageArticleList()
                    }
                }
                val weatherDeferred = async {
                    if (shouldReloadWeather) {
                        loadWeather()
                    }
                }
                val serviceCardDeferred = async {
                    if (shouldReloadServiceCard) {
                        loadServiceCardList()
                    }
                }

                awaitAll(articleDeferred, weatherDeferred, serviceCardDeferred)
            }

            publishFeedList(isLoadFinished = isInitialLoad || shouldReloadArticle)
        }
    }

    private suspend fun isSourceStale(source: FeedSource): Boolean {
        val lastFetchedAt = freshnessRepository.getLastFetchedAt(source)

        return freshnessRepository.isStale(source, lastFetchedAt)
    }

    private suspend fun loadWeather() {
        weatherRepository.fetchWeather(
            latitude = WEATHER_LATITUDE,
            longitude = WEATHER_LONGITUDE,
        ).onSuccess { response ->
            weatherBO = weatherUseCase.convertWeatherResponseToWeatherBO(response)

            freshnessRepository.setLastFetchedAt(FeedSource.WEATHER)
        }.onFailure {
            it.printStackTrace()
        }
    }

    private suspend fun loadServiceCardList() {
        serviceCardRepository.fetchServiceCardList(
            limit = SERVICE_CARD_PAGE_SIZE,
            skip = 0,
        ).onSuccess { response ->
            serviceCardList = serviceCardUseCase
                .convertServiceCardListResponseToServiceCardBO(response)
                .toPersistentList()

            freshnessRepository.setLastFetchedAt(FeedSource.SERVICE_CARD)
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun publishFeedList(isLoadFinished: Boolean = false) {
        _uiState.update {
            it.copy(
                feedList = createFeedList(),
                isArticleHasMore = articleHasMore,
                isArticleFetching = if (isLoadFinished) false else it.isArticleFetching,
                isArticleRefreshing = if (isLoadFinished) false else it.isArticleRefreshing,
            )
        }
    }

    private fun createFeedList(): PersistentList<FeedListItem> {
        return persistentListOf<FeedListItem>().mutate { feedList ->
            weatherBO?.let { feedList.add(FeedListItem.Weather(it)) }

            articleList.forEach { feedList.add(FeedListItem.Article(it)) }

            if (serviceCardList.isNotEmpty()) {
                feedList.add(
                    SERVICE_CARD_INDEX.coerceAtMost(feedList.size),
                    FeedListItem.ServiceCard(serviceCardList),
                )
            }
        }
    }

    private fun observeSavedArticleList() {
        viewModelScope.launch {
            articleRepository.getAllSavedArticleFlow()
                .collect { savedArticleList ->
                    val savedIdSet = savedArticleList.map { it.id }.toSet()

                    articleList = articleList.map { articleBO ->
                        articleBO.copy(isSaved = savedIdSet.contains(articleBO.id))
                    }.toPersistentList()

                    publishFeedList()
                }
        }
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            networkRepository.isOnline.collect { isOnline ->
                _uiState.update {
                    it.copy(isLowInternet = !isOnline)
                }
            }
        }
    }
}
