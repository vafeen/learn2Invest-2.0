package ru.surf.learn2invest.domain.network.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.CoinReview
import ru.surf.learn2invest.domain.network.NetworkPagedRepository
import ru.surf.learn2invest.domain.network.usecase.base.GetPagedMarketReviewUseCase
import javax.inject.Inject

/**
 * UseCase для получения постраничного списка обзоров рынка,
 * отсортированных по капитализации.
 *
 * @param networkPagedRepository Репозиторий для получения постраничных данных с сортировкой.
 */
class GetPagedMarketReviewSortedByMarketCapUseCase @Inject constructor(
    private val networkPagedRepository: NetworkPagedRepository
) : GetPagedMarketReviewUseCase {

    /**
     * Получает поток постраничных данных обзоров рынка,
     * отсортированных по капитализации.
     *
     * @param search Строка для поиска и фильтрации обзоров.
     * @param pageSize Размер страницы для пагинации.
     * @param onStart Лямбда, вызываемая при начале загрузки данных.
     * @param onEnd Лямбда, вызываемая при завершении загрузки данных.
     * @return Поток с постраничными данными типа [PagingData] с элементами [CoinReview].
     */
    override operator fun invoke(
        search: String,
        pageSize: Int,
        onStart: () -> Unit,
        onEnd: () -> Unit
    ): Flow<PagingData<CoinReview>> = networkPagedRepository.getPagedFlow(
        search = search,
        sortBy = NetworkPagedRepository.Companion.SortBy.MarketCap,
        pageSize = pageSize,
        onStart = onStart,
        onEnd = onEnd
    )
}
