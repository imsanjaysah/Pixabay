package com.sanjay.pixabay.paging.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sanjay.pixabay.BuildConfig
import com.sanjay.pixabay.constants.State
import com.sanjay.pixabay.data.repository.PixabayRepository
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import com.sanjay.pixabay.extensions.addToCompositeDisposable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PixabaySearchPagingDataSource @Inject constructor(private val repository: PixabayRepository) :
    PageKeyedDataSource<Int, PixabayImage>() {
    val disposable = CompositeDisposable()
    //LiveData object for state
    var state = MutableLiveData<State>()
    var searchQuery = MutableLiveData<String>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    /**
     * Creating the observable for specific page to call the API
     */
    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    //Retrying the API call
    fun retry() {
        if (retryCompletable != null) {
            retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe().addToCompositeDisposable(disposable)
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PixabayImage>
    ) {
        updateState(State.LOADING)
        val currentPage = 1
        val nextPage = currentPage + 1
        //Call api
        repository.searchImages(BuildConfig.API_KEY, searchQuery.value!!, currentPage, params.requestedLoadSize)
            .subscribe(
                { images ->
                    updateState(State.DONE)
                    callback.onResult(images, null, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadInitial(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PixabayImage>) {
        updateState(State.LOADING)
        val currentPage = params.key
        val nextPage = currentPage + 1
        //Call api
        repository.searchImages(BuildConfig.API_KEY, searchQuery.value!!, currentPage, params.requestedLoadSize)
            .subscribe(
                { images ->
                    updateState(State.DONE)
                    callback.onResult(images, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PixabayImage>) {
    }
}