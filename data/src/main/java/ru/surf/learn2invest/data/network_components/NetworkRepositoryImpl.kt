package ru.surf.learn2invest.data.network_components

import ru.surf.learn2invest.data.converters.AugmentedCoinReviewConverter
import ru.surf.learn2invest.data.converters.CoinPriceConverter
import ru.surf.learn2invest.data.converters.CoinReviewConverter
import ru.surf.learn2invest.data.services.coin_api_service.CoinAPIService
import ru.surf.learn2invest.data.services.coin_api_service.RetrofitLinks
import ru.surf.learn2invest.domain.domain_models.AugmentedCoinReview
import ru.surf.learn2invest.domain.domain_models.CoinPrice
import ru.surf.learn2invest.domain.domain_models.CoinReview
import ru.surf.learn2invest.domain.network.NetworkPagedRepository
import ru.surf.learn2invest.domain.network.NetworkRepository
import ru.surf.learn2invest.domain.network.ResponseResult
import javax.inject.Inject

/**
 * Репозиторий для получения данных с API
 */

internal class NetworkRepositoryImpl @Inject constructor(
    private val coinAPIService: CoinAPIService,
    private val augmentedCoinReviewConverter: AugmentedCoinReviewConverter,
    private val coinPriceConverter: CoinPriceConverter,
    private val coinReviewConverter: CoinReviewConverter,
) : NetworkRepository {
    override suspend fun getCoinReview(id: String): ResponseResult<AugmentedCoinReview> =
        try {
            val response = coinAPIService.getCoinReview(
                id = id.lowercase()
            )
            ResponseResult.Success(augmentedCoinReviewConverter.convert(response.data))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    override suspend fun getCoinHistory(id: String): ResponseResult<List<CoinPrice>> =
        try {
            val response = coinAPIService.getCoinHistory(
                id = id.lowercase(),
                interval = RetrofitLinks.INTERVAL,
                start = System.currentTimeMillis() - RetrofitLinks.WEEK,
                end = System.currentTimeMillis()
            )
            ResponseResult.Success(coinPriceConverter.convertList(response.data))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    override suspend fun getMarketReview(
        search: String,
        sortBy: NetworkPagedRepository.Companion.SortBy,
        pageNumber: Int,
        pageSize: Int
    ): ResponseResult<List<CoinReview>> =
        try {
            val response = coinAPIService.getMarketReview(
                pageNumber = pageNumber,
                pageSize = pageSize,
                sortBy = when (sortBy) {
                    NetworkPagedRepository.Companion.SortBy.MarketCap -> CoinAPIService.SORT_BY_MARKET_CAP
                    NetworkPagedRepository.Companion.SortBy.ChangePercent24h -> CoinAPIService.SORT_BY_CHANGE_PERCENT_24_H
                    NetworkPagedRepository.Companion.SortBy.PriceAsc -> CoinAPIService.SORT_BY_PRICE
                    NetworkPagedRepository.Companion.SortBy.PriceDesc -> CoinAPIService.SORT_BY_PRICE
                },
                sortOrder = when (sortBy) {
                    NetworkPagedRepository.Companion.SortBy.MarketCap -> CoinAPIService.SORT_ORDER_DESC
                    NetworkPagedRepository.Companion.SortBy.ChangePercent24h -> CoinAPIService.SORT_ORDER_DESC
                    NetworkPagedRepository.Companion.SortBy.PriceAsc -> CoinAPIService.SORT_ORDER_ASC
                    NetworkPagedRepository.Companion.SortBy.PriceDesc -> CoinAPIService.SORT_ORDER_DESC
                }, search = search
            )
            ResponseResult.Success(coinReviewConverter.convertList(response.data))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
}


