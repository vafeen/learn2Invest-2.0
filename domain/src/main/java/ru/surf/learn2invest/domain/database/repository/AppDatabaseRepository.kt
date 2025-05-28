package ru.surf.learn2invest.domain.database.repository

/**
 * Репозиторий для работы с базой данных приложения.
 */
interface AppDatabaseRepository {
    /**
     * Очищает все таблицы в базе данных.
     *
     * Выполняется асинхронно.
     */
    suspend fun clearAllTables()
}
