package ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview

internal sealed class MarketReviewFragmentIntent {
    data object FilterByMarketCap : MarketReviewFragmentIntent()
    data object FilterByPercent : MarketReviewFragmentIntent()
    data object FilterByPrice : MarketReviewFragmentIntent()
    data class SetSearchState(val isSearch: Boolean) :
        MarketReviewFragmentIntent()

    data class UpdateSearchRequest(val searchRequest: String) : MarketReviewFragmentIntent()

    data class UpdateData(val firstElement: Int, val lastElement: Int) :
        MarketReviewFragmentIntent()

    data object StartRealtimeUpdate : MarketReviewFragmentIntent()
    data object StopRealtimeUpdate : MarketReviewFragmentIntent()
    data class SetErrorState(val isError: Boolean) : MarketReviewFragmentIntent()
}