package ru.surf.learn2invest.domain.database.usecase

import ru.surf.learn2invest.domain.database.repository.AssetInvestRepository
import ru.surf.learn2invest.domain.domain_models.AssetInvest
import javax.inject.Inject

/**
 * UseCase для удаления записей об инвестициях в активы.
 *
 * @param repository Репозиторий для работы с инвестициями в активы.
 */
class DeleteAssetInvestUseCase @Inject constructor(
    private val repository: AssetInvestRepository
) {
    /**
     * Удаляет одну запись об инвестициях в актив.
     *
     * @param entity Запись для удаления.
     */
    suspend operator fun invoke(entity: AssetInvest) = repository.delete(entity)

    /**
     * Удаляет несколько записей об инвестициях в активы.
     *
     * @param entities Переменное число записей для удаления.
     */
    suspend operator fun invoke(vararg entities: AssetInvest) = repository.delete(*entities)
}
