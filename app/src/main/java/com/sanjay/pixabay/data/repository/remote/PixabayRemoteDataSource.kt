/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.data.repository.remote

import com.sanjay.pixabay.data.api.PixabayService
import com.sanjay.pixabay.data.repository.PixabayDataSource
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage

import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle remote operations
 *
 * @author Sanjay.Sah
 */
class PixabayRemoteDataSource @Inject constructor(private var remoteService: PixabayService) :
    PixabayDataSource {
    override fun searchImages(apiKey: String, query: String): Flowable<List<PixabayImage>> =
        remoteService.searchImages(apiKey, query).map {
            it.images
        }.toFlowable().take(1)
}