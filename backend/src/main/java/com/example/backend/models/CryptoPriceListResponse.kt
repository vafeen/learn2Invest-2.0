package com.example.backend.models

import kotlinx.serialization.Serializable

/**
 * Ответ с информацией о списке цен криптовалют.
 *
 * @property data Список объектов [CoinPriceResponse].
 * @property info Информация о списке (кол-во монет и время запроса).
 */
@Serializable
internal data class CryptoPriceListResponse(
    val data: List<CoinPriceResponse>,
    val info: Info = Info(
        coins_num = data.size,
        time = System.currentTimeMillis() / 1000
    )
)
