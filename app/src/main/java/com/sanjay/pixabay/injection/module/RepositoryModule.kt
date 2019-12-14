/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.injection.module

import com.sanjay.pixabay.data.repository.PixabayDataSource
import com.sanjay.pixabay.data.repository.local.PixabayLocalDataSource
import com.sanjay.pixabay.data.repository.remote.PixabayRemoteDataSource
import com.sanjay.pixabay.injection.annotations.Local
import com.sanjay.pixabay.injection.annotations.Remote
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Sanjay Sah
 */

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Local
    fun providesLocalDataSource(localDataSource: PixabayLocalDataSource): PixabayDataSource =
        localDataSource

    @Provides
    @Singleton
    @Remote
    fun providesRemoteDataSource(remoteDataSource: PixabayRemoteDataSource): PixabayDataSource =
        remoteDataSource

}