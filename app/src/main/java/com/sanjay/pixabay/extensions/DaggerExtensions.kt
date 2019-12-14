/*
 * DaggerExtensions.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.extensions
import android.content.Context
import com.sanjay.pixabay.PixabayApplication

/**
 * Created by Sanjay Sah
 */

val Context.appComponent
    get() = (applicationContext as PixabayApplication).appComponent
