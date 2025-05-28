package ru.surf.learn2invest.data.network_components.responses

/**
 * Обертка верхнего уровня для парсинга JSON-ответов API.
 *
 * @param T Тип данных, содержащихся в поле [data].
 * @property data Основные данные ответа.
 * @property info Информация о метаданных ответа, таких как количество монет и время запроса.
 */
internal data class APIWrapper<T>(
    val data: T,
    val info: Info = Info(
        coins_num = 0,
        time = System.currentTimeMillis() / 1000
    )
) {
    /**
     * Класс, содержащий метаданные ответа API.
     *
     * @property coins_num Количество монет в ответе.
     * @property time Время запроса в формате UNIX timestamp (секунды).
     */
    data class Info(
        val coins_num: Int,
        val time: Long
    )
}
