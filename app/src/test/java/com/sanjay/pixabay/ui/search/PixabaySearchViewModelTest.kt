package com.sanjay.pixabay.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import com.sanjay.pixabay.mockPagedList
import com.sanjay.pixabay.paging.datasource.PixabaySearchPagingDataSource
import com.sanjay.pixabay.paging.factory.PixabaySearchPagingDataSourceFactory
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PixabaySearchViewModelTest {

    private var viewModel: PixabaySearchViewModel? = null
    @Mock
    lateinit var pagingDataSourceFactory: PixabaySearchPagingDataSourceFactory

    @Mock
    lateinit var pagingDataSource: PixabaySearchPagingDataSource

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(pagingDataSourceFactory.dataSource).thenReturn(pagingDataSource)
        whenever(pagingDataSourceFactory.dataSource.state).thenReturn(MutableLiveData())

        viewModel = PixabaySearchViewModel(pagingDataSourceFactory)
    }

    @After
    fun close() {
        viewModel = null
    }

    @Test
    fun test_Initialization() {

        assertNotNull(viewModel?.images)
        assertNotNull(viewModel?.state)

    }

    @Test
    fun test_Retry() {
        viewModel?.retry()

        verify(pagingDataSourceFactory.dataSource).retry()

    }

    @Test
    fun test_ListIsEmpty() {

        assertTrue(viewModel!!.listIsEmpty())

        val post = mock<PixabayImage>()
        val pagedImagesList = MutableLiveData(mockPagedList(listOf(post)))

        viewModel?.images = pagedImagesList

        assertFalse(viewModel!!.listIsEmpty())

    }

    @Test
    fun test_Disposable() {
        viewModel?.disposable

        verify(pagingDataSourceFactory.dataSource).disposable

    }
}