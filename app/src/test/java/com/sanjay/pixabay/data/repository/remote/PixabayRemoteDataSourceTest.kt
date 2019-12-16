package com.sanjay.pixabay.data.repository.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.pixabay.data.api.PixabayService
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import com.sanjay.pixabay.data.repository.remote.model.PixabaySearchResponse
import io.reactivex.Single
import org.hamcrest.CoreMatchers
import org.junit.*
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PixabayRemoteDataSourceTest {
    @Mock
    lateinit var apiService: PixabayService

    private var remoteDataSource: PixabayRemoteDataSource? = null

    private val currentPage = 1
    private val limit = 20

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remoteDataSource = PixabayRemoteDataSource(apiService)
    }

    @After
    fun tearDown() {
        remoteDataSource = null
    }

    @Test
    fun searchImages() {
        val imagesList = emptyList<PixabayImage>()
        val imagesResponse = PixabaySearchResponse(imagesList)
        val observable = Single.just(imagesResponse)

        val apiKey = "api_key"
        val query = "fruits"

        val argumentCaptorInt = argumentCaptor<Int>()
        val argumentCaptorString = argumentCaptor<String>()

        whenever(apiService.searchImages(apiKey, query, currentPage, limit)).thenReturn(observable)

        remoteDataSource?.searchImages(apiKey, query, currentPage, limit)

        verify(apiService).searchImages(
            argumentCaptorString.capture(),
            argumentCaptorString.capture(),
            argumentCaptorInt.capture(),
            argumentCaptorInt.capture()
        )

        Assert.assertThat(
            apiService.searchImages(
                argumentCaptorString.firstValue,
                argumentCaptorString.secondValue,
                argumentCaptorInt.firstValue,
                argumentCaptorInt.secondValue
            ),
            CoreMatchers.instanceOf(Single::class.java)
        )
    }

}