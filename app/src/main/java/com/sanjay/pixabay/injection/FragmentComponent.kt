/*
 * FragmentComponent.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.injection

import com.sanjay.pixabay.injection.annotations.PerActivity
import com.sanjay.pixabay.injection.module.FragmentModule
import com.sanjay.pixabay.ui.detail.PixabayDetailImageFragment
import com.sanjay.pixabay.ui.search.PixabayImagesFragment
import dagger.Subcomponent

/**
 * @author Sanjay Sah
 */

@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: PixabayImagesFragment)
    fun inject(fragment: PixabayDetailImageFragment)
}