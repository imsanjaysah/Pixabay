package com.sanjay.pixabay.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import com.sanjay.pixabay.paging.factory.PixabaySearchPagingDataSourceFactory
import com.sanjay.pixabay.ui.BaseViewModel
import com.sanjay.pixabay.ui.search.PixabayImagesFragment.Companion.DEFAULT_SEARCH
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PixabaySearchViewModel @Inject constructor(private val pagingDataSourceFactory: PixabaySearchPagingDataSourceFactory) :
    BaseViewModel() {
    //LiveData object for Images
    var images: LiveData<PagedList<PixabayImage>>? = null
    //LiveData object for state
    var state = pagingDataSourceFactory.dataSource.state
    var searchQuery = pagingDataSourceFactory.dataSource.searchQuery

    init {
        //Setting up Paging for fetching the images in pagination
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        images = Transformations.switchMap(searchQuery) { _ ->
            LivePagedListBuilder<Int, PixabayImage>(
                pagingDataSourceFactory, config
            ).build()
        }

    }

    fun listIsEmpty(): Boolean {
        return images?.value?.isEmpty() ?: true
    }

    //Retrying the API call
    fun retry() {
        pagingDataSourceFactory.dataSource.retry()
    }

    override var disposable: CompositeDisposable
        get() = pagingDataSourceFactory.dataSource.disposable
        set(_) {}

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE_HINT = 20
    }
}