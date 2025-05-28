package com.example.backend.models

import kotlinx.serialization.Serializable

/**
 * Ответ с информацией о криптовалюте для обзора.
 *
 * @property id Идентификатор (ID) криптовалюты.
 * @property rank Ранг криптовалюты по капитализации.
 * @property symbol Символ криптовалюты (например, BTC, ETH).
 * @property name Название криптовалюты.
 * @property supply Текущее количество монет в обращении.
 * @property maxSupply Максимальное возможное количество монет.
 * @property marketCapUsd Рыночная капитализация в долларах США.
 * @property volumeUsd24Hr Объем торгов за последние 24 часа в долларах США.
 * @property priceUsd Текущая цена в долларах США.
 * @property changePercent24Hr Изменение цены за последние 24 часа в процентах.
 * @property vwap24Hr Средневзвешенная цена за последние 24 часа.
 */
@Serializable
internal data class CoinReviewResponse(
    val id: String,
    val rank: Int,
    val symbol: String,
    val name: String,
    val supply: Float,
    val maxSupply: Float,
    val marketCapUsd: Float,
    val volumeUsd24Hr: Float,
    val priceUsd: Float,
    val changePercent24Hr: Float,
    val vwap24Hr: Float
)
