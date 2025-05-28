package ru.surf.learn2invest.domain.network

import ru.surf.learn2invest.domain.domain_models.AugmentedCoinReview
import ru.surf.learn2invest.domain.domain_models.CoinPrice
import ru.surf.learn2invest.domain.domain_models.CoinReview

/**
 * Интерфейс репозитория для сетевых запросов, связанных с данными криптовалют.
 */
interface NetworkRepository {

    /**
     * Получает список обзоров рынка криптовалют с возможностью фильтрации и сортировки.
     *
     * @param search Строка для поиска и фильтрации обзоров.
     * @param sortBy Критерий сортировки.
     * @param pageNumber Номер страницы.
     * @param pageSize Размер страницы.
     * @return Результат с списком обзоров рынка в виде [CoinReview].
     */
    suspend fun getMarketReview(
        search: String,
        sortBy: NetworkPagedRepository.Companion.SortBy,
        pageNumber: Int,
        pageSize: Int
    ): ResponseResult<List<CoinReview>>

    /**
     * Получает историю цен криптовалюты по её идентификатору.
     *
     * @param id Идентификатор криптовалюты.
     * @return Результат с историей цен в виде списка [CoinPrice].
     */
    suspend fun getCoinHistory(id: String): ResponseResult<List<CoinPrice>>

    /**
     * Получает детальный обзор криптовалюты по её идентификатору.
     *
     * @param id Идентификатор криптовалюты.
     * @return Результат с детальным обзором в виде [AugmentedCoinReview].
     */
    suspend fun getCoinReview(id: String): ResponseResult<AugmentedCoinReview>
}
