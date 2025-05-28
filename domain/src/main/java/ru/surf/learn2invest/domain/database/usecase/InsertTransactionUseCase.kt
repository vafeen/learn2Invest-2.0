package ru.surf.learn2invest.domain.database.usecase

import ru.surf.learn2invest.domain.database.repository.TransactionRepository
import ru.surf.learn2invest.domain.domain_models.Transaction
import javax.inject.Inject

/**
 * UseCase для вставки транзакций в базу данных.
 *
 * @param repository Репозиторий для работы с транзакциями.
 */
class InsertTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    /**
     * Вставляет список транзакций.
     *
     * @param entities Список транзакций для вставки.
     */
    suspend operator fun invoke(entities: List<Transaction>) = repository.insert(entities)

    /**
     * Вставляет одну или несколько транзакций.
     *
     * @param entities Переменное число транзакций для вставки.
     */
    suspend operator fun invoke(vararg entities: Transaction) = repository.insert(*entities)
}
