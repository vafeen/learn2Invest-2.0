package ru.surf.learn2invest.domain.database.usecase

import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.database.repository.AssetInvestRepository
import ru.surf.learn2invest.domain.domain_models.AssetInvest
import javax.inject.Inject

/**
 * UseCase для получения информации об инвестициях в актив по его символу.
 *
 * @param repository Репозиторий для работы с инвестициями в активы.
 */
class GetBySymbolAssetInvestUseCase @Inject constructor(
    private val repository: AssetInvestRepository,
) {
    /**
     * Возвращает поток с информацией об инвестициях в актив по его символу.
     *
     * @param symbol Символ актива.
     * @return [Flow] с информацией об инвестициях в актив ([AssetInvest]) или null, если информация не найдена.
     */
    operator fun invoke(symbol: String): Flow<AssetInvest?> = repository.getBySymbol(symbol)
}
