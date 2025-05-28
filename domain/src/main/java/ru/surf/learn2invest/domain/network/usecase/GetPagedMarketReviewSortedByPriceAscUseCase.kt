package ru.surf.learn2invest.domain.network.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.CoinReview
import ru.surf.learn2invest.domain.network.NetworkPagedRepository
import ru.surf.learn2invest.domain.network.usecase.base.GetPagedMarketReviewUseCase
import javax.inject.Inject

class GetPagedMarketReviewSortedByPriceAscUseCase @Inject constructor(
    private val networkPagedRepository: NetworkPagedRepository
) : GetPagedMarketReviewUseCase {
    override operator fun invoke(
        search: String,
        pageSize: Int,
        onStart: () -> Unit,
        onEnd: () -> Unit
    ): Flow<PagingData<CoinReview>> = networkPagedRepository.getPagedFlow(
        search = search,
        sortBy = NetworkPagedRepository.Companion.SortBy.PriceAsc,
        pageSize = pageSize,
        onStart = onStart,
        onEnd = onEnd
    )
}