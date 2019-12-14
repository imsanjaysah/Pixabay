/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.data.repository.local

import com.sanjay.pixabay.data.repository.PixabayDataSource
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle local db operations
 *
 * @author Sanjay Sah
 */
class PixabayLocalDataSource @Inject constructor() : PixabayDataSource {
    override fun searchImages(apiKey: String, query: String): Flowable<List<PixabayImage>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}