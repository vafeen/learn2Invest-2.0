package ru.surf.learn2invest.domain.database.usecase

import ru.surf.learn2invest.domain.database.repository.AppDatabaseRepository
import javax.inject.Inject

/**
 * UseCase для очистки базы данных приложения.
 *
 * @param appDatabaseRepository Репозиторий для работы с базой данных приложения.
 */
class ClearAppDatabaseUseCase @Inject constructor(private val appDatabaseRepository: AppDatabaseRepository) {

    /**
     * Очищает все таблицы в базе данных.
     */
    suspend operator fun invoke() = appDatabaseRepository.clearAllTables()
}
