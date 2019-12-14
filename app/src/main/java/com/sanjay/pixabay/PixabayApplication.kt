/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay


import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.sanjay.pixabay.injection.AppComponent
import com.sanjay.pixabay.injection.DaggerAppComponent
import com.sanjay.pixabay.injection.module.ApiServiceModule
import com.sanjay.pixabay.injection.module.AppModule
import com.sanjay.pixabay.injection.module.RepositoryModule

import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class PixabayApplication : MultiDexApplication(), LifecycleObserver {
    lateinit var appComponent: AppComponent

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        configureExceptionLogging()
        buildComponent()

    }

    private fun configureExceptionLogging() {
        val default = Thread.getDefaultUncaughtExceptionHandler()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            Timber.e(e)
            default.uncaughtException(thread, e)
        }

        RxJavaPlugins.setErrorHandler(Timber::e)
    }

    /**
     * Generating the Dagger Graph
     */
    private fun buildComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .repositoryModule(RepositoryModule())
            .apiServiceModule(ApiServiceModule())
            .build()
    }
}