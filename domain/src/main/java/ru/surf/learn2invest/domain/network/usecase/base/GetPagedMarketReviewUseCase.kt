package ru.surf.learn2invest.domain.network.usecase.base

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.CoinReview

/**
 * Интерфейс UseCase для получения постраничного списка обзоров рынка криптовалют.
 */
interface GetPagedMarketReviewUseCase {

    /**
     * Получает поток постраничных данных обзоров рынка с возможностью фильтрации по поисковому запросу.
     *
     * @param search Строка для поиска и фильтрации обзоров.
     * @param pageSize Размер страницы для пагинации.
     * @param onStart Лямбда, вызываемая при начале загрузки данных.
     * @param onEnd Лямбда, вызываемая при завершении загрузки данных.
     * @return Поток с постраничными данными типа [PagingData] с элементами [CoinReview].
     */
    operator fun invoke(
        search: String,
        pageSize: Int,
        onStart: () -> Unit,
        onEnd: () -> Unit
    ): Flow<PagingData<CoinReview>>
}
