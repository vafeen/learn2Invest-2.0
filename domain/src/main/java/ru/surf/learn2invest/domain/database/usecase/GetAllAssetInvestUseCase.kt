package ru.surf.learn2invest.domain.database.usecase

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.database.repository.AssetInvestRepository
import ru.surf.learn2invest.domain.domain_models.AssetInvest
import javax.inject.Inject

/**
 * UseCase для получения всех записей об инвестициях в активы.
 *
 * @param repository Репозиторий для работы с инвестициями в активы.
 */
class GetAllAssetInvestUseCase @Inject constructor(
    private val repository: AssetInvestRepository
) {
    /**
     * Возвращает поток со списком всех записей об инвестициях в активы.
     *
     * @return [Flow] со списком [AssetInvest].
     */
    operator fun invoke(): Flow<List<AssetInvest>> = repository.getAllAsFlow()
}
