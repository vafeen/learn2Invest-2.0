package ru.surf.learn2invest.data.database_components.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.surf.learn2invest.data.converters.AssetInvestConverter
import ru.surf.learn2invest.data.database_components.dao.AssetInvestDao
import ru.surf.learn2invest.domain.database.repository.AssetInvestRepository
import ru.surf.learn2invest.domain.domain_models.AssetInvest
import javax.inject.Inject

/**
 * Реализация репозитория для работы с инвестициями в активы в базе данных.
 *
 * @param assetInvestDao DAO для доступа к данным инвестиций в активы.
 * @param assetInvestConverter Конвертер для преобразования между сущностями БД и моделями предметной области.
 */
internal class AssetInvestRepositoryImpl @Inject constructor(
    private val assetInvestDao: AssetInvestDao,
    private val assetInvestConverter: AssetInvestConverter,
) : AssetInvestRepository {

    /**
     * Получает все записи инвестиций в активы в виде потока.
     *
     * @return Flow со списком [AssetInvest].
     */
    override fun getAllAsFlow(): Flow<List<AssetInvest>> =
        assetInvestDao.getAllAsFlow().map {
            assetInvestConverter.convertABList(it)
        }

    /**
     * Вставляет список записей инвестиций в активы в базу данных.
     *
     * @param entities Список [AssetInvest] для вставки.
     */
    override suspend fun insert(entities: List<AssetInvest>) =
        assetInvestDao.insertAll(assetInvestConverter.convertBAList(entities))

    /**
     * Вставляет одну или несколько записей инвестиций в активы в базу данных.
     *
     * @param entities Переменное число [AssetInvest] для вставки.
     */
    override suspend fun insert(vararg entities: AssetInvest) =
        assetInvestDao.insertAll(assetInvestConverter.convertBAList(entities.toList()))

    /**
     * Удаляет запись инвестиций в активы из базы данных.
     *
     * @param entity [AssetInvest] для удаления.
     */
    override suspend fun delete(entity: AssetInvest) =
        assetInvestDao.delete(assetInvestConverter.convertBA(entity))

    /**
     * Удаляет одну или несколько записей инвестиций в активы из базы данных.
     *
     * @param entities Переменное число [AssetInvest] для удаления.
     */
    override suspend fun delete(vararg entities: AssetInvest) =
        assetInvestDao.deleteAll(assetInvestConverter.convertBAList(entities.toList()))

    /**
     * Получает запись инвестиций в активы по символу в виде потока.
     *
     * @param symbol Символ актива.
     * @return Flow с объектом [AssetInvest] или null, если запись не найдена.
     */
    override fun getBySymbol(symbol: String): Flow<AssetInvest?> =
        assetInvestDao.getBySymbol(symbol).map {
            it?.let { assetInvestConverter.convertAB(it) }
        }
}
