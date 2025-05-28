package ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview.paging

import androidx.recyclerview.widget.DiffUtil
import ru.surf.learn2invest.domain.domain_models.CoinReview

internal class MarketReviewPagingCallback : DiffUtil.ItemCallback<CoinReview>() {
    override fun areItemsTheSame(
        oldItem: CoinReview,
        newItem: CoinReview
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: CoinReview,
        newItem: CoinReview
    ): Boolean = oldItem == newItem
}