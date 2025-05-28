package ru.surf.learn2invest.domain.database.usecase

import ru.surf.learn2invest.domain.database.repository.AssetBalanceHistoryRepository
import ru.surf.learn2invest.domain.domain_models.AssetBalanceHistory
import javax.inject.Inject

/**
 * UseCase для вставки записей истории баланса активов в базу данных.
 *
 * @param repository Репозиторий для работы с историей баланса активов.
 */
class InsertAssetBalanceHistoryUseCase @Inject constructor(
    private val repository: AssetBalanceHistoryRepository
) {
    /**
     * Вставляет список записей истории баланса активов.
     *
     * @param entities Список записей для вставки.
     */
    suspend operator fun invoke(entities: List<AssetBalanceHistory>) = repository.insert(entities)

    /**
     * Вставляет одну или несколько записей истории баланса активов.
     *
     * @param entities Переменное число записей для вставки.
     */
    suspend operator fun invoke(vararg entities: AssetBalanceHistory) = repository.insert(*entities)
}
