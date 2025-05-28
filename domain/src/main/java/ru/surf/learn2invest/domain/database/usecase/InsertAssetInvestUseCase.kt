package ru.surf.learn2invest.domain.database.usecase

import ru.surf.learn2invest.domain.database.repository.AssetInvestRepository
import ru.surf.learn2invest.domain.domain_models.AssetInvest
import javax.inject.Inject

/**
 * UseCase для вставки записей об инвестициях в активы в базу данных.
 *
 * @param repository Репозиторий для работы с инвестициями в активы.
 */
class InsertAssetInvestUseCase @Inject constructor(
    private val repository: AssetInvestRepository
) {
    /**
     * Вставляет список записей об инвестициях в активы.
     *
     * @param entities Список записей для вставки.
     */
    suspend operator fun invoke(entities: List<AssetInvest>) = repository.insert(entities)

    /**
     * Вставляет одну или несколько записей об инвестициях в активы.
     *
     * @param entities Переменное число записей для вставки.
     */
    suspend operator fun invoke(vararg entities: AssetInvest) = repository.insert(*entities)
}
