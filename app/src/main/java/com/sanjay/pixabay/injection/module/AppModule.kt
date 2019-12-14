/*
 * AppModule.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.injection.module


import android.content.Context
import com.sanjay.pixabay.PixabayApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Sanjay Sah
 */

@Module
open class AppModule(private val application: PixabayApplication) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): PixabayApplication = application

}