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

internal class MarketReviewPagingAdapter @Inject constructor(
    private val loadCoinIconUseCase: LoadCoinIconUseCase,
    @ActivityContext private val context: Context
) :
    PagingDataAdapter<CoinReview, MarketReviewPagingAdapter.ViewHolder>(
        MarketReviewPagingCallback()
    ) {
//    /**
//     * Данные, которые отображаются в адаптере.
//     */
//    var data: List<CoinReview> = listOf()
//        set(value) {
//            val oldList = field
//            val diffCallback = MarketReviewAdapterDiffCallback(oldList, value)
//            val diffs = DiffUtil.calculateDiff(diffCallback)
//            field = value
//            diffs.dispatchUpdatesTo(this)
//        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.coin_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { coin ->
            holder.apply {
                // Отображение имени монеты с переносом строки, если оно слишком длинное
                coinTopTextInfo.text = if (coin.name.length > 10)
                    StringBuilder(coin.name).insert(10, '\n')
                else
                    coin.name
                coinBottomTextInfo.text = coin.symbol

                // Отображение цены монеты с учетом валюты
                coinTopNumericInfo.text =
                    NumberFormat.getInstance(Locale.US).apply {
                        maximumFractionDigits = 4
                    }.format(coin.priceUsd).getWithCurrency()

                // Установка цвета текста в зависимости от процента изменения цены
                if (coin.changePercent24Hr >= 0) {
                    coinBottomNumericInfo.setTextColor(coinBottomNumericInfo.context.getColor(R.color.increase))
                } else {
                    coinBottomNumericInfo.setTextColor(coinBottomNumericInfo.context.getColor(R.color.recession))
                }
                coinBottomNumericInfo.text = "${coin.changePercent24Hr.round()}%"

                // Загрузка иконки монеты
                loadCoinIconUseCase.invoke(coinIcon, coin.symbol)

                // Обработка клика по элементу списка
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
     * ViewHolder для отображения каждой строки в списке.
     * @param itemView представление элемента в RecyclerView.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinIcon = itemView.findViewById<ImageView>(R.id.coin_icon)
        val coinTopTextInfo = itemView.findViewById<TextView>(R.id.coin_name)
        val coinBottomTextInfo = itemView.findViewById<TextView>(R.id.coin_symbol)
        val coinTopNumericInfo = itemView.findViewById<TextView>(R.id.coin_top_numeric_info)
        val coinBottomNumericInfo = itemView.findViewById<TextView>(R.id.coin_bottom_numeric_info)
    }

}