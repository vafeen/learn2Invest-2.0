package com.example.backend

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil
import kotlin.random.Random


fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { json() }
        install(CORS) { anyHost() }

        routing {
            get("/") { call.respond("Test") }
            get("/assets") {
                val pageNumber = call.request.queryParameters["pageNumber"]?.toIntOrNull() ?: 1
                val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 20
                val sortBy = call.request.queryParameters["sortBy"] ?: "rank"
                val sortOrder = call.request.queryParameters["sortOrder"] ?: "asc"
                val searchQuery = call.request.queryParameters["search"]?.trim()?.lowercase()

                // Валидация параметров
                if (pageNumber < 1 || pageSize < 1 || pageSize > 100) {
                    call.respond(
                        HttpStatusCode.BadRequest, mapOf(
                            "error" to "Invalid pagination parameters",
                            "details" to "pageNumber must be ≥1, pageSize must be 1-100"
                        )
                    )
                    return@get
                }

                // Получаем и фильтруем монеты
                var allCoins = getUpdatedCoinsList()

                // Применяем поиск, если есть запрос
                if (!searchQuery.isNullOrEmpty()) {
                    allCoins = allCoins.filter { coin ->
                        coin.name.lowercase().contains(searchQuery) ||
                                coin.symbol.lowercase().contains(searchQuery) ||
                                coin.id.lowercase().contains(searchQuery)
                    }
                }

                // Сортируем
                allCoins = allCoins.sortedWith(getComparator(sortBy, sortOrder))

                val totalItems = allCoins.size
                val totalPages = ceil(totalItems.toDouble() / pageSize).toInt()

                if (pageNumber > totalPages) {
                    call.respond(
                        HttpStatusCode.BadRequest, mapOf(
                            "error" to "Page number out of range",
                            "maxPage" to totalPages
                        )
                    )
                    return@get
                }

                val paginatedCoins =
                    allCoins.chunked(pageSize).getOrElse(pageNumber - 1) { emptyList() }

                call.respond(
                    CryptoListResponse(
                        data = paginatedCoins,
                        info = Info(
                            coins_num = totalItems,
                            time = System.currentTimeMillis() / 1000
                        )
                    )
                )
            }
            get("/assets/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val coin = getUpdatedCoin(id) ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(CryptoDetailResponse(data = coin))
            }

            get("/assets/{id}/history") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val interval = call.request.queryParameters["interval"] ?: "d1"
                val start = call.request.queryParameters["start"]?.toLongOrNull()
                    ?: (System.currentTimeMillis() - 86400000 * 7)
                val end = call.request.queryParameters["end"]?.toLongOrNull()
                    ?: System.currentTimeMillis()

                if (interval != "d1") {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Only daily interval (d1) is supported")
                    )
                    return@get
                }

                val history = generateDailyHistory(id, start, end)
                call.respond(CryptoPriceListResponse(data = history))
            }
        }
    }.start(wait = true)
}

// Глобальное хранилище всех криптовалют
private val coinsStorage = generateInitialCoins()


// Генерация начальных данных
private fun generateInitialCoins(): MutableMap<String, CoinReviewResponse> {
    val result = mutableMapOf<String, CoinReviewResponse>()

    val count = 2000
    val popularCoins = listOf(
        CoinReviewResponse(
            id = "bitcoin",
            rank = 1,
            symbol = "BTC",
            name = "Bitcoin",
            supply = 18938281f,
            maxSupply = 21000000f,
            marketCapUsd = 1674213186750.20f,
            volumeUsd24Hr = 25159311166f,
            priceUsd = 84375.27f,
            changePercent24Hr = 0.75f,
            vwap24Hr = 84200.50f
        ),
        CoinReviewResponse(
            id = "ethereum",
            rank = 2,
            symbol = "ETH",
            name = "Ethereum",
            supply = 120000000f,
            maxSupply = 0f,
            marketCapUsd = 542184567890.12f,
            volumeUsd24Hr = 15432123456f,
            priceUsd = 4521.64f,
            changePercent24Hr = 1.25f,
            vwap24Hr = 4500.00f
        ),
        CoinReviewResponse(
            id = "xrp",
            rank = 3,
            symbol = "XRP",
            name = "XRP",
            supply = 45000000000f,
            maxSupply = 100000000000f,
            marketCapUsd = 45123456789.01f,
            volumeUsd24Hr = 1234567890f,
            priceUsd = 0.95f,
            changePercent24Hr = -0.35f,
            vwap24Hr = 0.94f
        )
    )

    val randomCoins = (1..(count - popularCoins.size)).map { index ->
        val id = "crypto-$index"
        val basePrice = maxOf(100f + Random.nextFloat() * 100, 0.01f)

        CoinReviewResponse(
            id = id,
            rank = popularCoins.size + index,
            symbol = "CRYPTO${index.toString().padStart(3, '0')}",
            name = "CryptoCoin$index",
            supply = Random.nextFloat() * 1000000000,
            maxSupply = Random.nextFloat() * 2000000000,
            marketCapUsd = basePrice * (Random.nextFloat() * 1000000),
            volumeUsd24Hr = Random.nextFloat() * 10000000000,
            priceUsd = basePrice,
            changePercent24Hr = (Random.nextFloat() * 2) - 1,
            vwap24Hr = basePrice * 0.99f
        )
    }

    (popularCoins + randomCoins).forEach {
        result[it.id] = it
    }
    return result
}

private fun getComparator(sortBy: String, sortOrder: String): Comparator<CoinReviewResponse> {
    val comparator = when (sortBy.lowercase()) {
        "marketCap" -> compareBy<CoinReviewResponse> { it.marketCapUsd }
        "price" -> compareBy<CoinReviewResponse> { it.priceUsd }
        "change" -> compareBy<CoinReviewResponse> { it.changePercent24Hr }
        else -> compareBy<CoinReviewResponse> { it.rank }
    }

    return if (sortOrder.lowercase() == "desc") {
        comparator.reversed()
    } else {
        comparator
    }
}

// Обновление всех полей монеты на ±5%
private fun updateCoinRandomly(coin: CoinReviewResponse): CoinReviewResponse {
    fun Float.randomize(): Float = this * (0.95f + Random.nextFloat() * 0.1f) // ±5%

    val newPrice = coin.priceUsd.randomize().coerceAtLeast(0.01f)

    return coin.copy(
        supply = coin.supply.randomize(),
        maxSupply = coin.maxSupply.randomize(),
        marketCapUsd = newPrice * (coin.supply.randomize()),
        volumeUsd24Hr = coin.volumeUsd24Hr.randomize(),
        priceUsd = newPrice,
        changePercent24Hr = (coin.changePercent24Hr * (0.9f + Random.nextFloat() * 0.2f)).coerceIn(
            -10f,
            10f
        ),
        vwap24Hr = newPrice * (0.95f + Random.nextFloat() * 0.1f)
    )
}

// Получить обновлённый список всех монет
private fun getUpdatedCoinsList(): List<CoinReviewResponse> {
    return coinsStorage.values.map { coin ->
        updateCoinRandomly(coin).also { updatedCoin ->
            coinsStorage[coin.id] = updatedCoin // Обновляем в хранилище
        }
    }
}

// Получить обновлённую монету по ID
private fun getUpdatedCoin(id: String): CoinReviewResponse? {
    return coinsStorage[id]?.let { coin ->
        updateCoinRandomly(coin).also { updatedCoin ->
            coinsStorage[id] = updatedCoin // Обновляем в хранилище
        }
    }
}

// Генератор исторических данных
internal fun generateDailyHistory(coinId: String, start: Long, end: Long): List<CoinPriceResponse> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val timeStep = (end - start) / 24

    val coin = getUpdatedCoin(coinId) ?: return emptyList()
    var currentPrice = coin.priceUsd

    return (0..23).map { i ->
        val time = start + i * timeStep
        val changePercent = (Random.nextFloat() * 0.1f) - 0.05f // ±5%
        currentPrice = maxOf(0.01f, currentPrice * (1 + changePercent))

        CoinPriceResponse(
            priceUsd = currentPrice,
            time = time,
            date = dateFormat.format(Date(time))
        )
    }
}

// Модели данных (остаются без изменений)
@Serializable
internal data class CoinPriceResponse(val priceUsd: Float, val time: Long, val date: String)

@Serializable
internal data class CryptoPriceListResponse(
    val data: List<CoinPriceResponse>,
    val info: Info = Info(
        coins_num = data.size,
        time = System.currentTimeMillis() / 1000
    )
)

@Serializable
internal data class CryptoListResponse(
    val data: List<CoinReviewResponse>,
    val info: Info = Info(
        coins_num = data.size,
        time = System.currentTimeMillis() / 1000
    )
)

@Serializable
internal data class CryptoDetailResponse(
    val data: CoinReviewResponse,
    val info: Info = Info(
        coins_num = 1,
        time = System.currentTimeMillis() / 1000
    )
)

@Serializable
internal data class Info(val coins_num: Int, val time: Long)

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