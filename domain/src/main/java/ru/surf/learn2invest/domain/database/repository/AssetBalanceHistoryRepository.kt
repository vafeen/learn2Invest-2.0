package ru.surf.learn2invest.domain.database.repository

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.AssetBalanceHistory

/**
 * Репозиторий для работы с историей баланса активов.
 */
interface AssetBalanceHistoryRepository {

    /**
     * Возвращает все записи истории баланса активов в виде потока.
     *
     * @return [Flow] со списком всех записей [AssetBalanceHistory].
     */
    fun getAllAsFlow(): Flow<List<AssetBalanceHistory>>

    /**
     * Вставляет список записей истории баланса активов в базу данных.
     *
     * @param entities Список записей для вставки.
     */
    suspend fun insert(entities: List<AssetBalanceHistory>)

    /**
     * Вставляет одну или несколько записей истории баланса активов в базу данных.
     *
     * @param entities Переменное число записей для вставки.
     */
    suspend fun insert(vararg entities: AssetBalanceHistory)

    /**
     * Вставляет записи истории баланса активов в базу данных с ограничением по количеству.
     *
     * @param limit Максимальное количество записей для вставки.
     * @param entities Список записей для вставки.
     */
    suspend fun insertByLimit(limit: Int, entities: List<AssetBalanceHistory>)

    /**
     * Вставляет записи истории баланса активов в базу данных с ограничением по количеству.
     *
     * @param limit Максимальное количество записей для вставки.
     * @param entities Переменное число записей для вставки.
     */
    suspend fun insertByLimit(limit: Int, vararg entities: AssetBalanceHistory)

    /**
     * Удаляет одну запись истории баланса активов из базы данных.
     *
     * @param entity Запись для удаления.
     */
    suspend fun delete(entity: AssetBalanceHistory)

    /**
     * Удаляет одну или несколько записей истории баланса активов из базы данных.
     *
     * @param entities Переменное число записей для удаления.
     */
    suspend fun delete(vararg entities: AssetBalanceHistory)
}
