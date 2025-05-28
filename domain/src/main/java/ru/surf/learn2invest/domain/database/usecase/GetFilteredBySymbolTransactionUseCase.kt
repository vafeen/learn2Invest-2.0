package ru.surf.learn2invest.domain.database.usecase

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.database.repository.TransactionRepository
import ru.surf.learn2invest.domain.domain_models.Transaction
import javax.inject.Inject

/**
 * UseCase для получения списка транзакций, отфильтрованных по символу актива.
 *
 * @param repository Репозиторий для работы с транзакциями.
 */
class GetFilteredBySymbolTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    /**
     * Возвращает поток со списком транзакций, отфильтрованных по символу актива.
     *
     * @param symbol Символ актива для фильтрации.
     * @return [Flow] со списком [Transaction], соответствующих символу.
     */
    operator fun invoke(symbol: String): Flow<List<Transaction>> =
        repository.getFilteredBySymbol(symbol)
}
