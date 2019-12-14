/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.data.repository


import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import com.sanjay.pixabay.injection.annotations.Local
import com.sanjay.pixabay.injection.annotations.Remote
import io.reactivex.Flowable
import javax.inject.Inject

/**
 *
 * @author Sanjay Sah.
 */
class PixabayRepository @Inject constructor(@Local private val localDataSource: PixabayDataSource, @Remote private val remoteDataSource: PixabayDataSource) :
    PixabayDataSource {
    override fun searchImages(apiKey: String, query: String): Flowable<List<PixabayImage>> =
        remoteDataSource.searchImages(apiKey, query)

}