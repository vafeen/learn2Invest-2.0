package ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.surf.learn2invest.domain.utils.launchMAIN
import ru.surf.learn2invest.presentation.R
import ru.surf.learn2invest.presentation.databinding.FragmentMarketReviewBinding
import ru.surf.learn2invest.presentation.ui.components.screens.fragments.common.BaseResFragment
import ru.surf.learn2invest.presentation.ui.components.screens.fragments.marketreview.paging.MarketReviewPagingAdapter
import ru.surf.learn2invest.presentation.utils.textListener
import javax.inject.Inject

/**
 * Фрагмент для отображения обзора рынка в HostActivity.
 * Предоставляет функционал:
 * - Отображение списка активов с пагинацией
 * - Сортировка по цене, капитализации и изменению за 24 часа
 * - Поиск по активам
 * - Обработка ошибок сети
 * - Поддержка темной/светлой темы
 */
@AndroidEntryPoint
internal class MarketReviewFragment : BaseResFragment() {

    /**
     * ViewModel для управления состоянием фрагмента
     */
    private val viewModel: MarketReviewFragmentViewModel by viewModels()

    /**
     * Адаптер для отображения данных с пагинацией
     */
    @Inject
    lateinit var adapter: MarketReviewPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMarketReviewBinding.inflate(layoutInflater)
        initListeners(binding)
        return binding.root
    }

    /**
     * Инициализация слушателей и подписок на изменения состояния
     * @param binding Привязка к layout фрагмента
     */
    private fun initListeners(binding: FragmentMarketReviewBinding) {
        binding.marketReviewRecyclerview.layoutManager = LinearLayoutManager(this.requireContext())
        binding.marketReviewRecyclerview.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            val error = when {
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                else -> null
            }
            viewModel.handleIntent(MarketReviewFragmentIntent.SetErrorState(error != null))
        }

        viewLifecycleOwner.lifecycleScope.launchMAIN {
            viewModel.state.collectLatest { state ->
                binding.apply {
                    // Обновление иконки сортировки по цене
                    if (state.filterByAsc) {
                        filterByPrice.setIconResource(R.drawable.arrow_top_green)
                        filterByPrice.setIconTintResource(R.color.label)
                    } else {
                        filterByPrice.setIconResource(R.drawable.arrow_bottom_red)
                        filterByPrice.setIconTintResource(R.color.recession)
                    }

                    // Управление видимостью элементов при загрузке/ошибке
                    marketReviewRecyclerview.isVisible = !state.isLoading
                    progressBar.isVisible = state.isLoading
                    marketReviewRecyclerview.isVisible = !state.isError
                    networkErrorTv.isVisible = state.isError
                    networkErrorIv.isVisible = state.isError

                    // Сброс поиска при выходе из режима поиска
                    if (!state.isSearch) {
                        searchEditText.text.clear()
                    }

                    // Управление элементами поиска
                    youSearch.isVisible = state.searchRequest.isBlank() && state.isSearch
                    cancelTV.isVisible = state.isSearch
                    filterByPrice.isVisible = !state.isSearch
                    filterByMarketcap.isVisible = !state.isSearch
                    filterByChangePercent24Hr.isVisible = !state.isSearch

                    // Обновление стилей кнопок фильтрации
                    state.filterState.also { filterState ->
                        val isDarkTheme = resources.configuration.uiMode and
                                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

                        filterByMarketcap.backgroundTintList = ColorStateList.valueOf(
                            getColorRes(
                                if (filterState == FilterState.FILTER_BY_MARKETCAP) {
                                    if (isDarkTheme)
                                        R.color.accent_background_dark
                                    else
                                        R.color.accent_background
                                } else {
                                    if (isDarkTheme)
                                        R.color.accent_button_dark
                                    else
                                        R.color.view_background
                                }
                            )
                        )

                        filterByChangePercent24Hr.backgroundTintList = ColorStateList.valueOf(
                            getColorRes(
                                if (filterState == FilterState.FILTER_BY_PERCENT) {
                                    if (isDarkTheme)
                                        R.color.accent_background_dark
                                    else
                                        R.color.accent_background
                                } else {
                                    if (isDarkTheme)
                                        R.color.accent_button_dark
                                    else
                                        R.color.view_background
                                }
                            )
                        )

                        filterByPrice.backgroundTintList = ColorStateList.valueOf(
                            getColorRes(
                                if (filterState == FilterState.FILTER_BY_PRICE) {
                                    if (isDarkTheme)
                                        R.color.accent_background_dark
                                    else
                                        R.color.accent_background
                                } else {
                                    if (isDarkTheme)
                                        R.color.accent_button_dark
                                    else
                                        R.color.view_background
                                }
                            )
                        )
                    }
                    adapter.submitData(state.pagingData)
                }
            }
        }

        lifecycleScope.launchMAIN {
            viewModel.effects.collectLatest { effect ->
                when (effect) {
                    MarketReviewFragmentEffect.RefreshData -> adapter.refresh()
                }
            }
        }

        binding.apply {
            filterByMarketcap.setOnClickListener {
                marketReviewRecyclerview.layoutManager?.scrollToPosition(0)
                viewModel.handleIntent(MarketReviewFragmentIntent.FilterByMarketCap)
            }

            filterByChangePercent24Hr.setOnClickListener {
                marketReviewRecyclerview.layoutManager?.scrollToPosition(0)
                viewModel.handleIntent(MarketReviewFragmentIntent.FilterByPercent)
            }

            filterByPrice.setOnClickListener {
                marketReviewRecyclerview.layoutManager?.scrollToPosition(0)
                viewModel.handleIntent(MarketReviewFragmentIntent.FilterByPrice)
            }

            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) viewModel.handleIntent(
                    MarketReviewFragmentIntent.SetSearchState(
                        true
                    )
                )
            }

            cancelTV.setOnClickListener {
                viewModel.handleIntent(MarketReviewFragmentIntent.SetSearchState(false))
                hideKeyboardFrom(requireContext(), searchEditText)
            }
            searchEditText.addTextChangedListener(textListener(afterTextChanged = {
                viewModel.handleIntent(
                    MarketReviewFragmentIntent.UpdateSearchRequest(searchRequest = it.toString())
                )
            }))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleIntent(MarketReviewFragmentIntent.StartRealtimeUpdate)
    }

    override fun onPause() {
        super.onPause()
        viewModel.handleIntent(MarketReviewFragmentIntent.StopRealtimeUpdate)
    }

    /**
     * Скрывает клавиатуру
     * @param context Контекст
     * @param view View для которого нужно скрыть клавиатуру
     */
    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}