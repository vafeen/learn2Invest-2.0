package ru.surf.learn2invest.data.database_components.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.surf.learn2invest.data.converters.TransactionConverter
import ru.surf.learn2invest.data.database_components.dao.TransactionDao
import ru.surf.learn2invest.domain.database.repository.TransactionRepository
import ru.surf.learn2invest.domain.domain_models.Transaction
import javax.inject.Inject

/**
 * Реализация репозитория для работы с транзакциями в базе данных.
 *
 * @param transactionDao DAO для доступа к данным транзакций.
 * @param transactionConverter Конвертер для преобразования между сущностями БД и моделями предметной области.
 */
internal class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val transactionConverter: TransactionConverter
) : TransactionRepository {

    /**
     * Получает все записи транзакций в виде потока.
     *
     * @return Flow со списком [Transaction].
     */
    override fun getAllAsFlow(): Flow<List<Transaction>> =
        transactionDao.getAllAsFlow().map {
            transactionConverter.convertABList(it)
        }

    /**
     * Вставляет список записей транзакций в базу данных.
     *
     * @param entities Список [Transaction] для вставки.
     */
    override suspend fun insert(entities: List<Transaction>) =
        transactionDao.insertAll(transactionConverter.convertBAList(entities))

    /**
     * Вставляет одну или несколько записей транзакций в базу данных.
     *
     * @param entities Переменное число [Transaction] для вставки.
     */
    override suspend fun insert(vararg entities: Transaction) =
        transactionDao.insertAll(transactionConverter.convertBAList(entities.toList()))

    /**
     * Удаляет запись транзакции из базы данных.
     *
     * @param entity [Transaction] для удаления.
     */
    override suspend fun delete(entity: Transaction) =
        transactionDao.delete(transactionConverter.convertBA(entity))

    /**
     * Удаляет одну или несколько записей транзакций из базы данных.
     *
     * @param entities Переменное число [Transaction] для удаления.
     */
    override suspend fun delete(vararg entities: Transaction) =
        transactionDao.deleteAll(transactionConverter.convertBAList(entities.toList()))

    /**
     * Получает записи транзакций, отфильтрованные по символу, в виде потока.
     *
     * @param symbol Символ для фильтрации транзакций.
     * @return Flow со списком [Transaction], отфильтрованных по символу.
     */
    override fun getFilteredBySymbol(symbol: String): Flow<List<Transaction>> =
        transactionDao.getFilteredBySymbol(filterSymbol = symbol).map {
            transactionConverter.convertABList(it)
        }
}
