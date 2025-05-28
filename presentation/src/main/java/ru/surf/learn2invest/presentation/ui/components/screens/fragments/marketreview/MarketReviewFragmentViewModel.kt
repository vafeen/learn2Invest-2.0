package ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import ru.surf.learn2invest.domain.domain_models.CoinReview
import ru.surf.learn2invest.domain.network.ResponseResult
import ru.surf.learn2invest.domain.network.usecase.GetCoinReviewUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByChangePercent24hUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByMarketCapUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByPriceAscUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByPriceDescUseCase
import ru.surf.learn2invest.domain.network.usecase.base.GetPagedMarketReviewUseCase
import ru.surf.learn2invest.domain.toCoinReview
import ru.surf.learn2invest.domain.utils.launchIO
import javax.inject.Inject

/**
 * ViewModel для экрана MarketReview, который управляет состоянием данных для отображения
 * информации о монетах и их фильтрации.
 *
 * @property getMarkerReviewUseCase Используется для получения списка всех рыночных обзоров.
 * @property insertSearchedCoinUseCase Используется для добавления монет в список поисковых запросов.
 * @property getAllSearchedCoinUseCase Используется для получения всех ранее добавленных монет в поисковых запросах.
 * @property getCoinReviewUseCase Используется для получения подробной информации о конкретной монете.
 * @property clearSearchedCoinUseCase Используется для очистки списка поисковых запросов.
 */
@HiltViewModel
internal class MarketReviewFragmentViewModel @Inject constructor(
    private val getPagedMarketReviewSortedByMarketCapUseCase: GetPagedMarketReviewSortedByMarketCapUseCase,
    private val getPagedMarketReviewSortedByChangePercent24hUseCase: GetPagedMarketReviewSortedByChangePercent24hUseCase,
    private val getPagedMarketReviewSortedByPriceAscUseCase: GetPagedMarketReviewSortedByPriceAscUseCase,
    private val getPagedMarketReviewSortedByPriceDescUseCase: GetPagedMarketReviewSortedByPriceDescUseCase,
    private val getCoinReviewUseCase: GetCoinReviewUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MarketReviewFragmentState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<MarketReviewFragmentEffect>()
    val effects = _effects.asSharedFlow()


    /**
     * Индекс первого элемента, который должен быть обновлен при загрузке новых данных.
     */
    private var firstUpdateElement = 0

    /**
     * Количество элементов, которые должны быть обновлены.
     */
    private var amountUpdateElement = 0

    private var pagedFlowJob: Job? = null
    private fun changePagedFlowJob(
        getPagedMarketReviewUseCase: GetPagedMarketReviewUseCase,
        search: String = ""
    ) {
        pagedFlowJob?.cancel()
        pagedFlowJob = viewModelScope.launchIO {
            getPagedMarketReviewUseCase.invoke(
                search = search,
                pageSize = 20,
                onStart = {
                    _state.update { it.copy(isLoading = true) }
                },
                onEnd = {
                    _state.update { it.copy(isLoading = false) }
                }
            ).cachedIn(viewModelScope).collectLatest { coins ->
                _state.update { it.copy(pagingData = coins) }
            }
        }
    }

    private var realtimeUpdateJob: Job? = null

    init {
        filterByMarketcap()
    }

    fun handleIntent(intent: MarketReviewFragmentIntent) {
        viewModelScope.launchIO {
            when (intent) {
                MarketReviewFragmentIntent.FilterByMarketCap -> {
                    filterByMarketcap()
                }

                MarketReviewFragmentIntent.FilterByPercent -> {
                    filterByPercent()
                }

                MarketReviewFragmentIntent.FilterByPrice -> {
                    filterByPrice()
                }

                is MarketReviewFragmentIntent.SetSearchState -> {
                    _state.update {
                        val copy = it.copy(isSearch = intent.isSearch)
                        if (!intent.isSearch) {
                            copy.copy(searchRequest = "")
                        } else copy
                    }
                }

                is MarketReviewFragmentIntent.UpdateData -> {
                    updateData(intent.firstElement, intent.lastElement)
                }

                is MarketReviewFragmentIntent.UpdateSearchRequest -> {
                    updateSearchRequest(intent.searchRequest)
                    updateCurrentFlowWithSearchRequest(intent.searchRequest)
                }


                MarketReviewFragmentIntent.StartRealtimeUpdate -> startRealtimeUpdate()
                MarketReviewFragmentIntent.StopRealtimeUpdate -> stopRealtimeUpdate()
                is MarketReviewFragmentIntent.SetErrorState -> setErrorState(intent.isError)
            }
        }
    }


    private fun setErrorState(isError: Boolean) {
        _state.update { it.copy(isError = isError) }
    }

    private fun startRealtimeUpdate() {
        realtimeUpdateJob = viewModelScope.launchIO {
            while (isActive) {
                _effects.emit(MarketReviewFragmentEffect.RefreshData)
                delay(20000)
            }
        }
    }

    private fun stopRealtimeUpdate() {
        realtimeUpdateJob?.cancel()
        realtimeUpdateJob = null
    }

    /**
     * Инициализирует данные, выполняет загрузку рыночных обзоров.
     */
    private suspend fun initData() {
//        when (val result = getMarkerReviewUseCase(1, 10)) {
//            is ResponseResult.Success -> {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        isError = false,
//                        data = result.value.toMutableList().filter {
//                            it.marketCapUsd > 0f && it.priceUsd > 0.1f
//                        }.sortedByDescending { it.marketCapUsd }
//                    )
//                }
//            }
//
//            is ResponseResult.Error -> {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        isError = true,
//                    )
//                }
//            }
//        }
    }

    /**
     * Инициализация данных, выполняется при создании ViewModel.
     * Получает рыночные обзоры и обновляет состояние данных.
     */
    init {
        viewModelScope.launchIO {
            initData()
        }
    }

    /**
     * Активирует состояние фильтра для выбранного элемента.
     *
     * @param filterState Идентификатор фильтра.
     */
    private fun activateFilterState(filterState: FilterState) {
        _state.update {
            it.copy(
                filterState = filterState,
//                filterByAsc = if (filterState != FilterState.FILTER_BY_PRICE) true else it.filterByAsc
            )
        }
    }

    /**
     * Сортирует данные по рыночной капитализации.
     */
    private fun filterByMarketcap() {
        activateFilterState(FilterState.FILTER_BY_MARKETCAP)
        changePagedFlowJob(getPagedMarketReviewSortedByMarketCapUseCase)

//        _state.update {
//            it.copy(data = it.data.sortedByDescending { element -> element.marketCapUsd })
//        }
    }

    private fun updateCurrentFlowWithSearchRequest(searchResuest: String) {
        val state = _state.value
        when (state.filterState) {
            FilterState.FILTER_BY_MARKETCAP -> changePagedFlowJob(
                getPagedMarketReviewUseCase = getPagedMarketReviewSortedByMarketCapUseCase,
                search = searchResuest
            )

            FilterState.FILTER_BY_PERCENT -> changePagedFlowJob(
                getPagedMarketReviewUseCase = getPagedMarketReviewSortedByChangePercent24hUseCase,
                search = searchResuest
            )

            FilterState.FILTER_BY_PRICE -> changePagedFlowJob(
                getPagedMarketReviewUseCase = if (state.filterByAsc) getPagedMarketReviewSortedByPriceAscUseCase else getPagedMarketReviewSortedByPriceDescUseCase,
                search = searchResuest
            )
        }
    }

    /**
     * Сортирует данные по процентному изменению за 24 часа.
     */
    private fun filterByPercent() {
        activateFilterState(FilterState.FILTER_BY_PERCENT)
        changePagedFlowJob(getPagedMarketReviewSortedByChangePercent24hUseCase)
//        _state.update {
//            it.copy(data = it.data.sortedByDescending { element -> element.changePercent24Hr })
//        }
    }

    /**
     * Сортирует данные по цене.
     */
    private fun filterByPrice() {
        val state = _state.value
        if (state.filterState == FilterState.FILTER_BY_PRICE) {
            if (!state.filterByAsc) {
                _state.update {
                    it.copy(filterByAsc = true)
                }
                changePagedFlowJob(getPagedMarketReviewSortedByPriceAscUseCase)
            } else {
                _state.update {
                    it.copy(filterByAsc = false)
                }
                changePagedFlowJob(getPagedMarketReviewSortedByPriceDescUseCase)
            }
        } else {
            activateFilterState(FilterState.FILTER_BY_PRICE)
            if (state.filterByAsc) {
                changePagedFlowJob(getPagedMarketReviewSortedByPriceAscUseCase)
            } else {
                changePagedFlowJob(getPagedMarketReviewSortedByPriceDescUseCase)
            }
        }
    }

    /**
     * Устанавливает состояние поиска и обновляет данные с учетом поискового запроса.
     * @param searchRequest Строка поискового запроса.
     */
    private fun updateSearchRequest(searchRequest: String = "") {
        _state.update {
            it.copy(
                searchRequest = searchRequest,
                dataBySearchRequest = if (searchRequest.isNotEmpty()) {
                    it.data.filter { element ->
                        searchRequest in element.name ||
                                searchRequest in element.symbol ||
                                searchRequest in element.id
                    }
                } else {
                    listOf()
                })

        }
    }

    /**
     * Обновляет данные для отображения в указанном диапазоне элементов.
     *
     * @param firstElement Индекс первого элемента.
     * @param lastElement Индекс последнего элемента.
     */
    private suspend fun updateData(firstElement: Int, lastElement: Int) {
        val tempUpdate = mutableListOf<CoinReview>()
        val state = state.value
        val updateDestinationLink = if (state.isSearch) state.searchedData else state.data
        if (updateDestinationLink.isNotEmpty()
            && firstElement != NO_POSITION
            && updateDestinationLink.size > lastElement
        ) {
            firstUpdateElement = firstElement
            amountUpdateElement = lastElement - firstElement + 1
            for (index in firstElement..lastElement) {
                when (val result =
                    getCoinReviewUseCase.invoke(updateDestinationLink[index].id)) {
                    is ResponseResult.Success -> {
                        tempUpdate.add(result.value.toCoinReview())
                    }

                    is ResponseResult.Error -> _state.update {
                        it.copy(isError = true)
                    }
                }
            }
            val tempUpdateId = tempUpdate.map { it.id }

            _state.update {
                if (state.isSearch) {
                    it.copy(searchedData = it.searchedData.mapNotNull { element ->
                        if (tempUpdateId.contains(element.id)) tempUpdate.find { updateElement ->
                            updateElement.id == element.id
                        }
                        else element
                    })
                } else {
                    it.copy(data = it.data.mapNotNull { element ->
                        if (tempUpdateId.contains(element.id)) tempUpdate.find { updateElement ->
                            updateElement.id == element.id
                        }
                        else element
                    })
                }
            }
        } else initData()
    }
}
