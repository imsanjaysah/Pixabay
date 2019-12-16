package com.sanjay.pixabay.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.pixabay.data.repository.local.PixabayLocalDataSource
import com.sanjay.pixabay.data.repository.remote.PixabayRemoteDataSource
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import io.reactivex.Flowable
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PixabayRepositoryTest {

    @Mock
    lateinit var localRepository: PixabayLocalDataSource
    @Mock
    lateinit var remoteRepository: PixabayRemoteDataSource

    private var repository: PixabayRepository? = null

    private val currentPage = 1
    private val limit = 20

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = PixabayRepository(localRepository, remoteRepository)
    }

    @After
    fun tearDown() {
        repository = null
    }

    @Test
    fun searchImages() {
        val imagesList = emptyList<PixabayImage>()
        val observable = Flowable.just(imagesList)

        val apiKey = "api_key"
        val query = "fruits"

        val argumentCaptorInt = argumentCaptor<Int>()
        val argumentCaptorString = argumentCaptor<String>()

        whenever(remoteRepository.searchImages(apiKey, query, currentPage, limit)).thenReturn(
            observable
        )

        repository?.searchImages(apiKey, query, currentPage, limit)

        verify(remoteRepository).searchImages(
            argumentCaptorString.capture(),
            argumentCaptorString.capture(),
            argumentCaptorInt.capture(),
            argumentCaptorInt.capture()
        )

        assertThat(
            remoteRepository.searchImages(
                argumentCaptorString.firstValue,
                argumentCaptorString.secondValue,
                argumentCaptorInt.firstValue,
                argumentCaptorInt.secondValue
            ),
            CoreMatchers.instanceOf(Flowable::class.java)
        )
    }
}