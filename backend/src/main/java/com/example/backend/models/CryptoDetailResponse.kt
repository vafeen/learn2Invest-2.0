package com.example.backend.models

import kotlinx.serialization.Serializable

/**
 * Ответ с детальной информацией о криптовалюте.
 *
 * @property data Объект [CoinReviewResponse] с детальной информацией.
 * @property info Информация о запросе (кол-во монет и время запроса).
 */
@Serializable
internal data class CryptoDetailResponse(
    val data: CoinReviewResponse,
    val info: Info = Info(
        coins_num = 1,
        time = System.currentTimeMillis() / 1000
    )
)
