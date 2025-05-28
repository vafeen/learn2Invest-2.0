package ru.surf.learn2invest.data.network_components.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.surf.learn2invest.domain.domain_models.CoinReview
import ru.surf.learn2invest.domain.domain_models.MyPagerConfig
import ru.surf.learn2invest.domain.network.NetworkPagedRepository
import ru.surf.learn2invest.domain.network.NetworkRepository
import ru.surf.learn2invest.domain.network.ResponseResult
import javax.inject.Inject

internal class NetworkPagedRepositoryImpl @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val coinReviewDataSourceFactory: CoinReviewDataSource.Factory
) : NetworkPagedRepository {
    override fun getPagedFlow(
        search: String,
        sortBy: NetworkPagedRepository.Companion.SortBy,
        pageSize: Int,
        onStart: () -> Unit,
        onEnd: () -> Unit
    ): Flow<PagingData<CoinReview>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize * 2,
            maxSize = pageSize * 3
        ),
        initialKey = 1,
        pagingSourceFactory = {
            coinReviewDataSourceFactory.create(
                search = search,
                sortBy = sortBy,
                pageSize = pageSize
            ) { config ->
                onStart()
                getData(config).also { onEnd() }
            }
        }
    ).flow


    private suspend fun getData(config: MyPagerConfig): ResponseResult<List<CoinReview>> =
        networkRepository.getMarketReview(
            search = config.search,
            sortBy = config.sortBy,
            pageNumber = config.pageNumber,
            pageSize = config.pageSize
        )
}