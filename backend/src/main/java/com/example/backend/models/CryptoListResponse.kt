package com.example.backend.models

import kotlinx.serialization.Serializable


/**
 * Ответ с информацией о списке криптовалют.
 *
 * @property data Список объектов [CoinReviewResponse].
 * @property info Информация о списке (кол-во монет и время запроса).
 */
@Serializable
internal data class CryptoListResponse(
    val data: List<CoinReviewResponse>,
    val info: Info = Info(
        coins_num = data.size,
        time = System.currentTimeMillis() / 1000
    )
)
