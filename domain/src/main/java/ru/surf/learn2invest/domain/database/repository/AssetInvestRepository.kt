package ru.surf.learn2invest.domain.database.repository

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.AssetInvest

/**
 * Репозиторий для работы с информацией об инвестициях в активы.
 */
interface AssetInvestRepository {

    /**
     * Возвращает все записи об инвестициях в активы в виде потока.
     *
     * @return [Flow] со списком всех записей [AssetInvest].
     */
    fun getAllAsFlow(): Flow<List<AssetInvest>>

    /**
     * Вставляет список записей об инвестициях в активы в базу данных.
     *
     * @param entities Список записей для вставки.
     */
    suspend fun insert(entities: List<AssetInvest>)

    /**
     * Вставляет одну или несколько записей об инвестициях в активы в базу данных.
     *
     * @param entities Переменное число записей для вставки.
     */
    suspend fun insert(vararg entities: AssetInvest)

    /**
     * Удаляет одну запись об инвестициях в активы из базы данных.
     *
     * @param entity Запись для удаления.
     */
    suspend fun delete(entity: AssetInvest)

    /**
     * Удаляет одну или несколько записей об инвестициях в активы из базы данных.
     *
     * @param entities Переменное число записей для удаления.
     */
    suspend fun delete(vararg entities: AssetInvest)

    /**
     * Возвращает запись об инвестициях в актив по его символу в виде потока.
     *
     * @param symbol Символ актива.
     * @return [Flow] с записью [AssetInvest] или null, если запись не найдена.
     */
    fun getBySymbol(symbol: String): Flow<AssetInvest?>
}
