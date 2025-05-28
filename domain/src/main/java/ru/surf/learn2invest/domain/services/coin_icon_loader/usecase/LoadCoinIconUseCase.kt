package ru.surf.learn2invest.domain.services.coin_icon_loader.usecase

import android.widget.ImageView
import ru.surf.learn2invest.domain.services.coin_icon_loader.CoinIconLoader
import javax.inject.Inject

/**
 * UseCase для загрузки и отображения иконки криптовалюты в ImageView.
 *
 * @param coinIconLoader Сервис для загрузки иконок криптовалют.
 */
class LoadCoinIconUseCase @Inject constructor(private val coinIconLoader: CoinIconLoader) {

    /**
     * Загружает иконку криптовалюты по символу и отображает её в переданном ImageView.
     *
     * @param imageView ImageView, в который будет загружена иконка.
     * @param symbol Символ криптовалюты (например, BTC, ETH).
     * @return Объект запроса загрузки иконки.
     */
    operator fun invoke(imageView: ImageView, symbol: String): CoinIconLoader.Request =
        coinIconLoader.loadIcon(imageView, symbol)
}
