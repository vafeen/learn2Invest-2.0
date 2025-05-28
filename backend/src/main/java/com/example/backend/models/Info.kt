package com.example.backend.models

import kotlinx.serialization.Serializable

/**
 * Информация о результате запроса.
 *
 * @property coins_num Количество монет, возвращённых в ответе.
 * @property time Время выполнения запроса в формате UNIX timestamp (секунды).
 */
@Serializable
internal data class Info(
    val coins_num: Int,
    val time: Long
)
