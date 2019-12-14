/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.data.repository

import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import io.reactivex.Flowable

/**
 * @author Sanjay Sah
 */
interface PixabayDataSource {

    fun searchImages(apiKey: String, query: String): Flowable<List<PixabayImage>>

}