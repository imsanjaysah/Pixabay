/*
 * ViewModelModule.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanjay.pixabay.injection.ViewModelFactory
import com.sanjay.pixabay.ui.search.PixabaySearchViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
open class ViewModelModule {

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(PixabaySearchViewModel::class)
    fun providesPixabaySeacrhViewModel(viewModel: PixabaySearchViewModel): ViewModel = viewModel

}