package ru.surf.learn2invest.data.database_components.repository

import ru.surf.learn2invest.data.database_components.L2IDatabase
import ru.surf.learn2invest.domain.database.repository.AppDatabaseRepository
import javax.inject.Inject

/**
 * Реализация репозитория для работы с базой данных приложения.
 *
 * @param db Экземпляр базы данных L2IDatabase.
 */
internal class AppDatabaseRepositoryImpl @Inject constructor(
    private val db: L2IDatabase
) : AppDatabaseRepository {

    /**
     * Очищает все таблицы в базе данных.
     */
    override suspend fun clearAllTables() = db.clearAllTables()
}
