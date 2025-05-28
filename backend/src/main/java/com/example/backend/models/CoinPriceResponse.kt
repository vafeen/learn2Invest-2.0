package com.example.backend.models

import kotlinx.serialization.Serializable

/**
 * Ответ с информацией о цене криптовалюты.
 *
 * @property priceUsd Цена криптовалюты в долларах США.
 * @property time Время в формате UNIX timestamp (миллисекунды).
 * @property date Дата в строковом формате.
 */
@Serializable
internal data class CoinPriceResponse(
    val priceUsd: Float,
    val time: Long,
    val date: String
)
