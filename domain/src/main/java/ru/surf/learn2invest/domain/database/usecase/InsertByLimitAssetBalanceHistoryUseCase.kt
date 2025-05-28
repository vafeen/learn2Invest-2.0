package ru.surf.learn2invest.domain.database.usecase

import ru.surf.learn2invest.domain.database.repository.AssetBalanceHistoryRepository
import ru.surf.learn2invest.domain.domain_models.AssetBalanceHistory
import javax.inject.Inject

/**
 * UseCase для вставки записей истории баланса активов с ограничением по количеству.
 *
 * @param repository Репозиторий для работы с историей баланса активов.
 */
class InsertByLimitAssetBalanceHistoryUseCase @Inject constructor(
    private val repository: AssetBalanceHistoryRepository
) {
    /**
     * Вставляет список записей истории баланса активов с ограничением по количеству.
     *
     * @param limit Максимальное количество записей для вставки.
     * @param entities Список записей для вставки.
     */
    suspend operator fun invoke(limit: Int, entities: List<AssetBalanceHistory>) =
        repository.insertByLimit(limit, entities)

    /**
     * Вставляет одну или несколько записей истории баланса активов с ограничением по количеству.
     *
     * @param limit Максимальное количество записей для вставки.
     * @param entities Переменное число записей для вставки.
     */
    suspend operator fun invoke(limit: Int, vararg entities: AssetBalanceHistory) =
        repository.insertByLimit(limit, *entities)
}
