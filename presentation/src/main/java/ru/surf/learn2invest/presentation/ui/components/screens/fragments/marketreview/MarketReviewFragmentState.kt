package ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview

import androidx.paging.PagingData
import ru.surf.learn2invest.domain.domain_models.CoinReview

/**
 * Состояние экрана "Обзор рынка".
 * Содержит все необходимые данные для отображения текущего состояния UI:
 * - Данные для отображения в списке (с пагинацией)
 * - Параметры сортировки и фильтрации
 * - Состояния загрузки и ошибок
 * - Параметры поиска
 *
 * @property pagingData Данные для отображения в списке с пагинацией
 * @property filterByAsc Направление сортировки (true - по возрастанию)
 * @property filterState Текущий активный фильтр
 * @property isError Флаг наличия ошибки загрузки данных
 * @property isLoading Флаг выполнения загрузки данных
 * @property isSearch Флаг активности режима поиска
 * @property searchRequest Текущий текст поискового запроса
 */
internal data class MarketReviewFragmentState(
    val pagingData: PagingData<CoinReview> = PagingData.empty(),
    val filterByAsc: Boolean = true,
    val filterState: FilterState = FilterState.FILTER_BY_MARKETCAP,
    val isError: Boolean = false,
    val isLoading: Boolean = true,
    val isSearch: Boolean = false,
    val searchRequest: String = "",
)