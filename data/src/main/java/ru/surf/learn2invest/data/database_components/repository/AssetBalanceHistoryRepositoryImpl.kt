package ru.surf.learn2invest.data.database_components.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.surf.learn2invest.data.converters.AssetBalanceHistoryConverter
import ru.surf.learn2invest.data.database_components.dao.AssetBalanceHistoryDao
import ru.surf.learn2invest.domain.database.repository.AssetBalanceHistoryRepository
import ru.surf.learn2invest.domain.domain_models.AssetBalanceHistory
import javax.inject.Inject

/**
 * Реализация репозитория для работы с историей баланса активов в базе данных.
 *
 * @param assetBalanceHistoryDao DAO для доступа к данным истории баланса активов.
 * @param assetBalanceHistoryConverter Конвертер для преобразования между сущностями БД и моделями предметной области.
 */
internal class AssetBalanceHistoryRepositoryImpl @Inject constructor(
    private val assetBalanceHistoryDao: AssetBalanceHistoryDao,
    private val assetBalanceHistoryConverter: AssetBalanceHistoryConverter
) : AssetBalanceHistoryRepository {

    /**
     * Получает все записи истории баланса активов в виде потока.
     *
     * @return Flow со списком [AssetBalanceHistory].
     */
    override fun getAllAsFlow(): Flow<List<AssetBalanceHistory>> =
        assetBalanceHistoryDao.getAllAsFlow().map {
            assetBalanceHistoryConverter.convertABList(it)
        }

    /**
     * Вставляет список записей истории баланса активов в базу данных.
     *
     * @param entities Список [AssetBalanceHistory] для вставки.
     */
    override suspend fun insert(entities: List<AssetBalanceHistory>) =
        assetBalanceHistoryDao.insertAll(assetBalanceHistoryConverter.convertBAList(entities))

    /**
     * Вставляет одну или несколько записей истории баланса активов в базу данных.
     *
     * @param entities Переменное число [AssetBalanceHistory] для вставки.
     */
    override suspend fun insert(vararg entities: AssetBalanceHistory) =
        assetBalanceHistoryDao.insertAll(assetBalanceHistoryConverter.convertBAList(entities.toList()))

    /**
     * Вставляет список записей истории баланса активов в базу данных с ограничением по количеству.
     *
     * @param limit Максимальное количество записей для вставки.
     * @param entities Список [AssetBalanceHistory] для вставки.
     */
    override suspend fun insertByLimit(limit: Int, entities: List<AssetBalanceHistory>) =
        assetBalanceHistoryDao.insertByLimit(
            limit,
            assetBalanceHistoryConverter.convertBAList(entities)
        )

    /**
     * Вставляет одну или несколько записей истории баланса активов в базу данных с ограничением по количеству.
     *
     * @param limit Максимальное количество записей для вставки.
     * @param entities Переменное число [AssetBalanceHistory] для вставки.
     */
    override suspend fun insertByLimit(limit: Int, vararg entities: AssetBalanceHistory) =
        assetBalanceHistoryDao.insertByLimit(
            limit,
            assetBalanceHistoryConverter.convertBAList(entities.toList())
        )

    /**
     * Удаляет запись истории баланса активов из базы данных.
     *
     * @param entity [AssetBalanceHistory] для удаления.
     */
    override suspend fun delete(entity: AssetBalanceHistory) =
        assetBalanceHistoryDao.delete(assetBalanceHistoryConverter.convertBA(entity))

    /**
     * Удаляет одну или несколько записей истории баланса активов из базы данных.
     *
     * @param entities Переменное число [AssetBalanceHistory] для удаления.
     */
    override suspend fun delete(vararg entities: AssetBalanceHistory) =
        assetBalanceHistoryDao.deleteAll(assetBalanceHistoryConverter.convertBAList(entities.toList()))
}
