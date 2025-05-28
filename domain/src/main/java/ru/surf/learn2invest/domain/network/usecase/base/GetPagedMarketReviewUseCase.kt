package ru.surf.learn2invest.domain.network.usecase.base

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.CoinReview

interface GetPagedMarketReviewUseCase {
    operator fun invoke(
        search: String,
        pageSize: Int,
        onStart: () -> Unit,
        onEnd: () -> Unit
    ): Flow<PagingData<CoinReview>>
}