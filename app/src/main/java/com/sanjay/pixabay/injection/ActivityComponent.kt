/*
 * ActivityComponent.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.injection

import com.sanjay.pixabay.ui.search.PixabaySearchActivity
import com.sanjay.pixabay.injection.annotations.PerActivity
import com.sanjay.pixabay.injection.module.ActivityModule
import dagger.Subcomponent

/**
 * @author Sanjay Sah
 */

@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: PixabaySearchActivity)


}