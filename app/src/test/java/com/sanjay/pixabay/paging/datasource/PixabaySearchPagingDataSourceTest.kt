package com.sanjay.pixabay.paging.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.pixabay.BuildConfig
import com.sanjay.pixabay.RxImmediateSchedulerRule
import com.sanjay.pixabay.constants.State
import com.sanjay.pixabay.data.repository.PixabayRepository
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import io.reactivex.Flowable
import org.junit.*
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class PixabaySearchPagingDataSourceTest {
    private var pagingDataSource: PixabaySearchPagingDataSource? = null

    @Mock
    lateinit var repository: PixabayRepository

    private var loadParams = PageKeyedDataSource.LoadParams(1, 20)

    @Mock
    lateinit var loadCallback: PageKeyedDataSource.LoadCallback<Int, PixabayImage>

    private var loadInitialParams = PageKeyedDataSource.LoadInitialParams<Int>(20, false)

    @Mock
    lateinit var loadInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, PixabayImage>

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private var observerState = mock<Observer<State>>()
    private val apiKey = BuildConfig.API_KEY
    private val query = "fruits"
    private val currentPage = 1


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pagingDataSource = PixabaySearchPagingDataSource(repository)
        pagingDataSource!!.searchQuery.value = query

        //whenever(BuildConfig.API_KEY).thenReturn(apiKey)
    }

    @After
    fun tearDown() {
        pagingDataSource = null
    }

    @Test
    fun loadInitial_Success() {
        val imagesList = emptyList<PixabayImage>()
        val observable = Flowable.just(imagesList)

        whenever(
            repository.searchImages(
                apiKey,
                query,
                currentPage,
                loadInitialParams.requestedLoadSize
            )
        ).thenReturn(
            observable
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.DONE

        verify(repository).searchImages(
            apiKey,
            query,
            currentPage,
            loadInitialParams.requestedLoadSize
        )
        verify(loadInitialCallback).onResult(imagesList, null, 2)
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }
    }

    @Test
    fun loadInitial_Error() {
        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.searchImages(
                apiKey,
                query,
                currentPage,
                loadInitialParams.requestedLoadSize
            )
        ).thenReturn(
            Flowable.error(response)
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.ERROR

        verify(repository).searchImages(
            apiKey,
            query,
            currentPage,
            loadInitialParams.requestedLoadSize
        )

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }

    }

    @Test
    fun loadInitial_Retry() {

        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.searchImages(
                apiKey,
                query,
                currentPage,
                loadInitialParams.requestedLoadSize
            )
        ).thenReturn(Flowable.error(response))

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadInitial(loadInitialParams, loadInitialCallback)

        pagingDataSource!!.retry()

        verify(repository, times(2)).searchImages(
            apiKey,
            query,
            currentPage,
            loadInitialParams.requestedLoadSize
        )
    }
    @Test
    fun loadAfter_Success() {
        val imagesList = emptyList<PixabayImage>()
        val observable = Flowable.just(imagesList)

        whenever(
            repository.searchImages(
                apiKey,
                query,
                currentPage,
                loadParams.requestedLoadSize
            )
        ).thenReturn(
            observable
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadAfter(loadParams, loadCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.DONE

        verify(repository).searchImages(
            apiKey,
            query,
            currentPage,
            loadParams.requestedLoadSize
        )
        verify(loadCallback).onResult(imagesList, 2)
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }
    }

    @Test
    fun loadAfter_Error() {
        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.searchImages(
                apiKey,
                query,
                currentPage,
                loadParams.requestedLoadSize
            )
        ).thenReturn(
            Flowable.error(response)
        )

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadAfter(loadParams, loadCallback)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedLoadingState = State.LOADING
        val expectedDoneState = State.ERROR

        verify(repository).searchImages(
            apiKey,
            query,
            currentPage,
            loadParams.requestedLoadSize
        )

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            val (loadingState, doneState) = allValues
            assertEquals(loadingState, expectedLoadingState)
            assertEquals(doneState, expectedDoneState)
        }

    }

    @Test
    fun loadAfter_Retry() {

        val errorMessage = "Error response"
        val response = Throwable(errorMessage)

        whenever(
            repository.searchImages(
                apiKey,
                query,
                currentPage,
                loadParams.requestedLoadSize
            )
        ).thenReturn(Flowable.error(response))

        pagingDataSource!!.state.observeForever(observerState)

        pagingDataSource!!.loadAfter(loadParams, loadCallback)

        pagingDataSource!!.retry()

        verify(repository, times(2)).searchImages(
            apiKey,
            query,
            currentPage,
            loadParams.requestedLoadSize
        )
    }
}