package com.sanjay.pixabay.data.repository.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class PixabaySearchResponse(
    @SerializedName("hits") val images: List<PixabayImage>
)

@Parcelize
data class PixabayImage(
    val id: String,
    val user: String,
    val likes: Int,
    val comments: Int,
    val favorites: Int,
    val tags: String

) : Parcelable