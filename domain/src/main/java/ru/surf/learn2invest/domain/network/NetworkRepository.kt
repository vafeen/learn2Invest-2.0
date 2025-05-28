package ru.surf.learn2invest.domain.network

import ru.surf.learn2invest.domain.domain_models.AugmentedCoinReview
import ru.surf.learn2invest.domain.domain_models.CoinPrice
import ru.surf.learn2invest.domain.domain_models.CoinReview

interface NetworkRepository {
    suspend fun getMarketReview(
        search: String,
        sortBy: NetworkPagedRepository.Companion.SortBy,
        pageNumber: Int,
        pageSize: Int
    ): ResponseResult<List<CoinReview>>

    suspend fun getCoinHistory(id: String): ResponseResult<List<CoinPrice>>
    suspend fun getCoinReview(id: String): ResponseResult<AugmentedCoinReview>
}
