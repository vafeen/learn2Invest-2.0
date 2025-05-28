package ru.surf.learn2invest.domain.domain_models

import ru.surf.learn2invest.domain.network.NetworkPagedRepository

/**
 * Конфигурация для пагинации с параметрами поиска и сортировки.
 *
 * @property search Строка для поиска.
 * @property sortBy Критерий сортировки.
 * @property pageNumber Номер страницы.
 * @property pageSize Размер страницы.
 */
data class MyPagerConfig(
    val search: String,
    val sortBy: NetworkPagedRepository.Companion.SortBy,
    val pageNumber: Int,
    val pageSize: Int
)