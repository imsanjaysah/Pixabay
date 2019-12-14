package com.sanjay.pixabay.paging.factory

import androidx.paging.DataSource
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import com.sanjay.pixabay.paging.datasource.PixabaySearchPagingDataSource
import javax.inject.Inject

class PixabaySearchPagingDataSourceFactory @Inject constructor(val dataSource: PixabaySearchPagingDataSource) :
    DataSource.Factory<Int, PixabayImage>() {

    override fun create(): DataSource<Int, PixabayImage> {
        return dataSource
    }
}