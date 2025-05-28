package ru.surf.learn2invest.domain.network

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.CoinReview

interface NetworkPagedRepository {
    fun getPagedFlow(
        search: String,
        sortBy: SortBy,
        pageSize: Int,
        onStart: () -> Unit,
        onEnd: () -> Unit
    ): Flow<PagingData<CoinReview>>

    companion object {
        enum class SortBy {
            MarketCap,
            ChangePercent24h,
            PriceAsc,
            PriceDesc,
        }
    }
}