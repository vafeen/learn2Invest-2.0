package ru.surf.learn2invest.domain.network

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.CoinReview

/**
 * Интерфейс репозитория для получения постраничных данных обзоров рынка криптовалют.
 */
interface NetworkPagedRepository {

    /**
     * Получает поток постраничных данных обзоров рынка с возможностью фильтрации и сортировки.
     *
     * @param search Строка для поиска и фильтрации обзоров.
     * @param sortBy Критерий сортировки.
     * @param pageSize Размер страницы для пагинации.
     * @param onStart Лямбда, вызываемая при начале загрузки данных.
     * @param onEnd Лямбда, вызываемая при завершении загрузки данных.
     * @return Поток с постраничными данными типа [PagingData] с элементами [CoinReview].
     */
    fun getPagedFlow(
        search: String,
        sortBy: SortBy,
        pageSize: Int,
        onStart: () -> Unit,
        onEnd: () -> Unit
    ): Flow<PagingData<CoinReview>>

    companion object {
        /**
         * Критерии сортировки для получения обзоров рынка.
         */
        enum class SortBy {
            MarketCap,
            ChangePercent24h,
            PriceAsc,
            PriceDesc,
        }
    }
}
