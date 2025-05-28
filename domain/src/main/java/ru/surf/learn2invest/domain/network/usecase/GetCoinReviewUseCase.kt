package ru.surf.learn2invest.domain.network.usecase

import ru.surf.learn2invest.domain.domain_models.AugmentedCoinReview
import ru.surf.learn2invest.domain.network.NetworkRepository
import ru.surf.learn2invest.domain.network.ResponseResult
import javax.inject.Inject

/**
 * UseCase для получения детального обзора криптовалюты.
 *
 * @param repository Репозиторий для сетевых запросов.
 */
class GetCoinReviewUseCase @Inject constructor(
    private val repository: NetworkRepository
) {
    /**
     * Получает детальный обзор криптовалюты по её идентификатору.
     *
     * @param id Идентификатор криптовалюты.
     * @return Результат с детальным обзором в виде [AugmentedCoinReview].
     */
    suspend operator fun invoke(id: String): ResponseResult<AugmentedCoinReview> = repository.getCoinReview(id)
}
