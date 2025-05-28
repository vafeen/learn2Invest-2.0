package ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
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
import ru.surf.learn2invest.domain.network.usecase.GetCoinReviewUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByChangePercent24hUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByMarketCapUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByPriceAscUseCase
import ru.surf.learn2invest.domain.network.usecase.GetPagedMarketReviewSortedByPriceDescUseCase
import ru.surf.learn2invest.domain.network.usecase.base.GetPagedMarketReviewUseCase
import ru.surf.learn2invest.domain.utils.launchIO
import javax.inject.Inject

/**
 * ViewModel для экрана "Обзор рынка".
 * Управляет:
 * - Получением и пагинацией данных о криптовалютах
 * - Сортировкой по различным параметрам (капитализация, цена, изменение цены)
 * - Поиском по списку криптовалют
 * - Автоматическим обновлением данных
 * - Обработкой состояний загрузки и ошибок
 *
 * @param getPagedMarketReviewSortedByMarketCapUseCase UseCase для получения данных, отсортированных по рыночной капитализации
 * @param getPagedMarketReviewSortedByChangePercent24hUseCase UseCase для получения данных, отсортированных по изменению цены за 24 часа
 * @param getPagedMarketReviewSortedByPriceAscUseCase UseCase для получения данных, отсортированных по цене (по возрастанию)
 * @param getPagedMarketReviewSortedByPriceDescUseCase UseCase для получения данных, отсортированных по цене (по убыванию)
 * @param getCoinReviewUseCase UseCase для получения детальной информации о криптовалюте
 */
@HiltViewModel
internal class MarketReviewFragmentViewModel @Inject constructor(
    private val getPagedMarketReviewSortedByMarketCapUseCase: GetPagedMarketReviewSortedByMarketCapUseCase,
    private val getPagedMarketReviewSortedByChangePercent24hUseCase: GetPagedMarketReviewSortedByChangePercent24hUseCase,
    private val getPagedMarketReviewSortedByPriceAscUseCase: GetPagedMarketReviewSortedByPriceAscUseCase,
    private val getPagedMarketReviewSortedByPriceDescUseCase: GetPagedMarketReviewSortedByPriceDescUseCase,
    private val getCoinReviewUseCase: GetCoinReviewUseCase,
) : ViewModel() {

    /**
     * Текущее состояние экрана
     */
    private val _state = MutableStateFlow(MarketReviewFragmentState())
    val state = _state.asStateFlow()

    /**
     * Поток для side-эффектов (единичных событий)
     */
    private val _effects = MutableSharedFlow<MarketReviewFragmentEffect>()
    val effects = _effects.asSharedFlow()

    /**
     * Job для управления пагинацией данных
     */
    private var pagedFlowJob: Job? = null

    /**
     * Job для управления автоматическим обновлением данных
     */
    private var realtimeUpdateJob: Job? = null

    init {
        filterByMarketCap()
    }

    /**
     * Обрабатывает интенты (пользовательские действия)
     * @param intent Действие, которое нужно обработать
     */
    fun handleIntent(intent: MarketReviewFragmentIntent) {
        viewModelScope.launchIO {
            when (intent) {
                MarketReviewFragmentIntent.FilterByMarketCap -> filterByMarketCap()
                MarketReviewFragmentIntent.FilterByPercent -> filterByPercent()
                MarketReviewFragmentIntent.FilterByPrice -> filterByPrice()
                is MarketReviewFragmentIntent.SetSearchState -> handleSearchState(intent.isSearch)
                is MarketReviewFragmentIntent.UpdateSearchRequest -> handleSearchRequest(intent.searchRequest)
                MarketReviewFragmentIntent.StartRealtimeUpdate -> startRealtimeUpdate()
                MarketReviewFragmentIntent.StopRealtimeUpdate -> stopRealtimeUpdate()
                is MarketReviewFragmentIntent.SetErrorState -> setErrorState(intent.isError)
            }
        }
    }

    /**
     * Устанавливает состояние ошибки
     * @param isError Флаг наличия ошибки
     */
    private fun setErrorState(isError: Boolean) {
        _state.update { it.copy(isError = isError) }
    }

    /**
     * Запускает автоматическое обновление данных каждые 20 секунд
     */
    private fun startRealtimeUpdate() {
        realtimeUpdateJob = viewModelScope.launchIO {
            while (isActive) {
                _effects.emit(MarketReviewFragmentEffect.RefreshData)
                delay(20000)
            }
        }
    }

    /**
     * Останавливает автоматическое обновление данных
     */
    private fun stopRealtimeUpdate() {
        realtimeUpdateJob?.cancel()
        realtimeUpdateJob = null
    }

    /**
     * Активирует указанный фильтр
     * @param filterState Тип фильтра для активации
     */
    private fun activateFilterState(filterState: FilterState) {
        _state.update { it.copy(filterState = filterState) }
    }

    /**
     * Устанавливает сортировку по рыночной капитализации
     */
    private fun filterByMarketCap() {
        activateFilterState(FilterState.FILTER_BY_MARKETCAP)
        changePagedFlowJob(getPagedMarketReviewSortedByMarketCapUseCase)
    }

    /**
     * Обновляет текущий поток данных с учетом поискового запроса
     * @param searchRequest Текст поискового запроса
     */
    private fun updateCurrentFlowWithSearchRequest(searchRequest: String) {
        val state = _state.value
        when (state.filterState) {
            FilterState.FILTER_BY_MARKETCAP -> changePagedFlowJob(
                getPagedMarketReviewUseCase = getPagedMarketReviewSortedByMarketCapUseCase,
                search = searchRequest
            )
            FilterState.FILTER_BY_PERCENT -> changePagedFlowJob(
                getPagedMarketReviewUseCase = getPagedMarketReviewSortedByChangePercent24hUseCase,
                search = searchRequest
            )
            FilterState.FILTER_BY_PRICE -> changePagedFlowJob(
                getPagedMarketReviewUseCase = if (state.filterByAsc)
                    getPagedMarketReviewSortedByPriceAscUseCase
                else
                    getPagedMarketReviewSortedByPriceDescUseCase,
                search = searchRequest
            )
        }
    }

    /**
     * Устанавливает сортировку по изменению цены за 24 часа
     */
    private fun filterByPercent() {
        activateFilterState(FilterState.FILTER_BY_PERCENT)
        changePagedFlowJob(getPagedMarketReviewSortedByChangePercent24hUseCase)
    }

    /**
     * Устанавливает сортировку по цене (переключает между возрастанием/убыванием)
     */
    private fun filterByPrice() {
        val state = _state.value
        if (state.filterState == FilterState.FILTER_BY_PRICE) {
            _state.update { it.copy(filterByAsc = !state.filterByAsc) }
            changePagedFlowJob(
                if (state.filterByAsc)
                    getPagedMarketReviewSortedByPriceDescUseCase
                else
                    getPagedMarketReviewSortedByPriceAscUseCase
            )
        } else {
            activateFilterState(FilterState.FILTER_BY_PRICE)
            changePagedFlowJob(
                if (state.filterByAsc)
                    getPagedMarketReviewSortedByPriceAscUseCase
                else
                    getPagedMarketReviewSortedByPriceDescUseCase
            )
        }
    }

    /**
     * Обрабатывает изменение состояния поиска
     * @param isSearch Флаг активности поиска
     */
    private fun handleSearchState(isSearch: Boolean) {
        _state.update {
            val newState = it.copy(isSearch = isSearch)
            if (!isSearch) newState.copy(searchRequest = "") else newState
        }
    }

    /**
     * Обрабатывает поисковый запрос
     * @param searchRequest Текст поискового запроса
     */
    private fun handleSearchRequest(searchRequest: String) {
        updateSearchRequest(searchRequest)
        updateCurrentFlowWithSearchRequest(searchRequest)
    }

    /**
     * Обновляет поисковый запрос в состоянии
     * @param searchRequest Текст поискового запроса
     */
    private fun updateSearchRequest(searchRequest: String = "") {
        _state.update { it.copy(searchRequest = searchRequest) }
    }

    /**
     * Изменяет текущий поток пагинации
     * @param getPagedMarketReviewUseCase UseCase для получения данных
     * @param search Поисковый запрос (необязательный)
     */
    private fun changePagedFlowJob(
        getPagedMarketReviewUseCase: GetPagedMarketReviewUseCase,
        search: String = ""
    ) {
        pagedFlowJob?.cancel()
        pagedFlowJob = viewModelScope.launchIO {
            getPagedMarketReviewUseCase.invoke(
                search = search,
                pageSize = 20,
                onStart = { _state.update { it.copy(isLoading = true) } },
                onEnd = { _state.update { it.copy(isLoading = false) } }
            ).cachedIn(viewModelScope).collectLatest { coins ->
                _state.update { it.copy(pagingData = coins) }
            }
        }
    }
}