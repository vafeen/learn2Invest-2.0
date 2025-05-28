package ru.surf.learn2invest.domain.database.usecase

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.database.repository.TransactionRepository
import ru.surf.learn2invest.domain.domain_models.Transaction
import javax.inject.Inject

/**
 * UseCase для получения всех транзакций.
 *
 * @param repository Репозиторий для работы с транзакциями.
 */
class GetAllTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    /**
     * Возвращает поток со списком всех транзакций.
     *
     * @return [Flow] со списком [Transaction].
     */
    operator fun invoke(): Flow<List<Transaction>> = repository.getAllAsFlow()
}
