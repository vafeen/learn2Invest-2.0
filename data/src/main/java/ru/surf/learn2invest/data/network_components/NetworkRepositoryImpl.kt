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
 * Репозиторий для получения данных из API.
 *
 * @param coinAPIService Сервис для взаимодействия с Coin API.
 * @param augmentedCoinReviewConverter Конвертер для [AugmentedCoinReview].
 * @param coinPriceConverter Конвертер для [CoinPrice].
 * @param coinReviewConverter Конвертер для [CoinReview].
 */
internal class NetworkRepositoryImpl @Inject constructor(
    private val coinAPIService: CoinAPIService,
    private val augmentedCoinReviewConverter: AugmentedCoinReviewConverter,
    private val coinPriceConverter: CoinPriceConverter,
    private val coinReviewConverter: CoinReviewConverter,
) : NetworkRepository {

    /**
     * Получает детальный обзор криптовалюты по ID.
     *
     * @param id ID криптовалюты.
     * @return [ResponseResult] с [AugmentedCoinReview] или ошибкой.
     */
    override suspend fun getCoinReview(id: String): ResponseResult<AugmentedCoinReview> =
        try {
            val response = coinAPIService.getCoinReview(
                id = id.lowercase()
            )
            ResponseResult.Success(augmentedCoinReviewConverter.convert(response.data))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    /**
     * Получает историю цен криптовалюты по ID.
     *
     * @param id ID криптовалюты.
     * @return [ResponseResult] со списком [CoinPrice] или ошибкой.
     */
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

    /**
     * Получает список обзоров рынка криптовалют с пагинацией и сортировкой.
     *
     * @param search Строка поиска.
     * @param sortBy Параметр сортировки.
     * @param pageNumber Номер страницы.
     * @param pageSize Размер страницы.
     * @return [ResponseResult] со списком [CoinReview] или ошибкой.
     */
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
