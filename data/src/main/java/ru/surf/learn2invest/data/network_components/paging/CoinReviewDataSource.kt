package ru.surf.learn2invest.data.network_components.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.surf.learn2invest.domain.domain_models.CoinReview
import ru.surf.learn2invest.domain.domain_models.MyPagerConfig
import ru.surf.learn2invest.domain.network.NetworkPagedRepository
import ru.surf.learn2invest.domain.network.ResponseResult

internal class CoinReviewDataSource @AssistedInject constructor(
    @Assisted private val search: String,
    @Assisted private val sortBy: NetworkPagedRepository.Companion.SortBy,
    @Assisted private val pageSize: Int,
    @Assisted private val getData: suspend (pagerConfig: MyPagerConfig) -> ResponseResult<List<CoinReview>>,
) : PagingSource<Int, CoinReview>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinReview> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        return when (val result = getData(
            MyPagerConfig(
                search = search,
                sortBy = sortBy,
                pageNumber = pageNumber,
                pageSize = pageSize
            )
        )) {
            is ResponseResult.Error<*> -> {
                LoadResult.Error(result.e)
            }

            is ResponseResult.Success<List<CoinReview>> -> {
                val data = result.value
                LoadResult.Page(
                    data = data,
                    prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1,
                    nextKey = if (data.size < pageSize) null else pageNumber + 1
                )
            }
        }

    }

    // эта функция должна загрузить страницу в которой обновились данные
    override fun getRefreshKey(state: PagingState<Int, CoinReview>): Int? {
        // это индекс элемента который запрашивался последним
        val anchorPosition = state.anchorPosition ?: return null
        // конвертировать индекс в номер страницу
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    @AssistedFactory
    interface Factory {
        fun create(
            search: String,
            sortBy: NetworkPagedRepository.Companion.SortBy,
            pageSize: Int,
            getData: suspend (pagerConfig: MyPagerConfig) -> ResponseResult<List<CoinReview>>,
        ): CoinReviewDataSource
    }

}