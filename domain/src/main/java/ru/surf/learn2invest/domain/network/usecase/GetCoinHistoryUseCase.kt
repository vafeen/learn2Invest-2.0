package ru.surf.learn2invest.domain.network.usecase

import ru.surf.learn2invest.domain.domain_models.CoinPrice
import ru.surf.learn2invest.domain.network.NetworkRepository
import ru.surf.learn2invest.domain.network.ResponseResult
import javax.inject.Inject

/**
 * UseCase для получения исторических данных по цене криптовалюты.
 *
 * @param repository Репозиторий для сетевых запросов.
 */
class GetCoinHistoryUseCase @Inject constructor(
    private val repository: NetworkRepository
) {
    /**
     * Получает историю цен криптовалюты по её идентификатору.
     *
     * @param id Идентификатор криптовалюты.
     * @return Результат с историей цен в виде списка [CoinPrice].
     */
    suspend operator fun invoke(id: String): ResponseResult<List<CoinPrice>> = repository.getCoinHistory(id)
}
