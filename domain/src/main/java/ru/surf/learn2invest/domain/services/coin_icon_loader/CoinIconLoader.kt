package ru.surf.learn2invest.domain.services.coin_icon_loader

import android.widget.ImageView

/**
 * Интерфейс для загрузки иконок криптовалют.
 */
interface CoinIconLoader {

    /**
     * Загружает иконку криптовалюты по символу и отображает её в переданном ImageView.
     *
     * @param imageView ImageView, в который будет загружена иконка.
     * @param symbol Символ криптовалюты (например, BTC, ETH).
     * @return Объект запроса загрузки иконки, который можно отменить.
     */
    fun loadIcon(imageView: ImageView, symbol: String): Request

    /**
     * Интерфейс для управления запросом загрузки иконки.
     */
    interface Request {
        /**
         * Отменяет текущий запрос загрузки иконки.
         */
        fun cancel()
    }
}
