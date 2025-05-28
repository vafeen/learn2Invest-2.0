package ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ActivityContext
import ru.surf.learn2invest.domain.domain_models.CoinReview
import ru.surf.learn2invest.domain.services.coin_icon_loader.usecase.LoadCoinIconUseCase
import ru.surf.learn2invest.presentation.R
import ru.surf.learn2invest.presentation.ui.components.screens.fragments.asset_review.AssetReviewActivity
import ru.surf.learn2invest.presentation.utils.getWithCurrency
import ru.surf.learn2invest.presentation.utils.round
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Адаптер для отображения списка криптовалют с пагинацией.
 * Обрабатывает:
 * - Загрузку и отображение данных о криптовалютах
 * - Подгрузку иконок криптовалют
 * - Обработку кликов по элементам списка
 *
 * @param loadCoinIconUseCase UseCase для загрузки иконок криптовалют
 * @param context Контекст активности (передается через Hilt)
 */
internal class MarketReviewPagingAdapter @Inject constructor(
    private val loadCoinIconUseCase: LoadCoinIconUseCase,
    @ActivityContext private val context: Context
) : PagingDataAdapter<CoinReview, MarketReviewPagingAdapter.ViewHolder>(
    MarketReviewPagingCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.coin_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { coin ->
            with(holder) {
                // Отображение названия с переносом строки при необходимости
                coinTopTextInfo.text = if (coin.name.length > 10)
                    StringBuilder(coin.name).insert(10, '\n')
                else
                    coin.name

                coinBottomTextInfo.text = coin.symbol

                // Форматирование цены с валютой
                coinTopNumericInfo.text =
                    NumberFormat.getInstance(Locale.US).apply {
                        maximumFractionDigits = 4
                    }.format(coin.priceUsd).getWithCurrency()

                // Установка цвета в зависимости от изменения цены
                val colorRes = if (coin.changePercent24Hr >= 0)
                    R.color.increase
                else
                    R.color.recession
                coinBottomNumericInfo.setTextColor(context.getColor(colorRes))
                coinBottomNumericInfo.text = "${coin.changePercent24Hr.round()}%"

                // Загрузка иконки
                loadCoinIconUseCase.invoke(coinIcon, coin.symbol)

                // Обработка клика
                itemView.setOnClickListener {
                    context.startActivity(
                        AssetReviewActivity.newIntent(
                            context as AppCompatActivity,
                            coin.id,
                            coin.name,
                            coin.symbol
                        )
                    )
                }
            }
        }
    }

    /**
     * ViewHolder для элемента списка криптовалют.
     * Содержит ссылки на все View элементы макета.
     *
     * @property coinIcon ImageView для иконки криптовалюты
     * @property coinTopTextInfo TextView для названия криптовалюты
     * @property coinBottomTextInfo TextView для символа криптовалюты
     * @property coinTopNumericInfo TextView для цены криптовалюты
     * @property coinBottomNumericInfo TextView для изменения цены (в процентах)
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinIcon: ImageView = itemView.findViewById(R.id.coin_icon)
        val coinTopTextInfo: TextView = itemView.findViewById(R.id.coin_name)
        val coinBottomTextInfo: TextView = itemView.findViewById(R.id.coin_symbol)
        val coinTopNumericInfo: TextView = itemView.findViewById(R.id.coin_top_numeric_info)
        val coinBottomNumericInfo: TextView = itemView.findViewById(R.id.coin_bottom_numeric_info)
    }
}