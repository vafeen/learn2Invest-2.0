package ru.surf.learn2invest.presentation.ui.components.screens.fragments.asset_overview

import ru.surf.learn2invest.domain.domain_models.AssetInvest

/**
 * Состояние информации о монете. Может быть данными или пустым результатом.
 */
internal data class AssetOverviewState(
    val finResult: FinResult? = null, // Финансовый результат
    val coinCostResult: String? = null, // Общая стоимость активов с учетом текущей цены
    val coinPriceChangesResult: String? = null, // Изменения цены актива
    val price: Float? = null,
    val marketCap: Float? = null,
    val coin: AssetInvest? = null,
)