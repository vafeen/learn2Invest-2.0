package ru.surf.learn2invest.domain.database.repository

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.Transaction

/**
 * Репозиторий для работы с транзакциями.
 */
interface TransactionRepository {

    /**
     * Возвращает все транзакции в виде потока.
     *
     * @return [Flow] со списком всех транзакций.
     */
    fun getAllAsFlow(): Flow<List<Transaction>>

    /**
     * Вставляет список транзакций в базу данных.
     *
     * @param entities Список транзакций для вставки.
     */
    suspend fun insert(entities: List<Transaction>)

    /**
     * Вставляет одну или несколько транзакций в базу данных.
     *
     * @param entities Переменное число транзакций для вставки.
     */
    suspend fun insert(vararg entities: Transaction)

    /**
     * Удаляет одну транзакцию из базы данных.
     *
     * @param entity Транзакция для удаления.
     */
    suspend fun delete(entity: Transaction)

    /**
     * Удаляет одну или несколько транзакций из базы данных.
     *
     * @param entities Переменное число транзакций для удаления.
     */
    suspend fun delete(vararg entities: Transaction)

    /**
     * Возвращает транзакции, отфильтрованные по символу актива, в виде потока.
     *
     * @param symbol Символ актива для фильтрации транзакций.
     * @return [Flow] со списком транзакций, соответствующих символу.
     */
    fun getFilteredBySymbol(symbol: String): Flow<List<Transaction>>
}
