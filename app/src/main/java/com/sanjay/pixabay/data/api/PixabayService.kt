/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.data.api


import com.sanjay.pixabay.data.repository.remote.model.PixabaySearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**Interface where all the api used in app are defined.
 * @author Sanjay.Sah
 */
interface PixabayService {

    /**
     * Api for fetching Images list
     */
    @GET()
    fun searchImages(@Query("key") key: String, @Query("q") query: String): Single<PixabaySearchResponse>

}