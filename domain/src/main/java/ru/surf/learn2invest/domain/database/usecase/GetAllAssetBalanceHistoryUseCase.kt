package ru.surf.learn2invest.domain.database.usecase

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.database.repository.AssetBalanceHistoryRepository
import ru.surf.learn2invest.domain.domain_models.AssetBalanceHistory
import javax.inject.Inject

/**
 * UseCase для получения всей истории баланса активов.
 *
 * @param repository Репозиторий для работы с историей баланса активов.
 */
class GetAllAssetBalanceHistoryUseCase @Inject constructor(
    private val repository: AssetBalanceHistoryRepository
) {
    /**
     * Возвращает поток со списком всех записей истории баланса активов.
     *
     * @return [Flow] со списком [AssetBalanceHistory].
     */
    operator fun invoke(): Flow<List<AssetBalanceHistory>> = repository.getAllAsFlow()
}
