package ru.surf.learn2invest.domain.network

data class MyPagerConfig(
    val search: String,
    val sortBy: NetworkPagedRepository.Companion.SortBy,
    val pageNumber: Int,
    val pageSize: Int
)