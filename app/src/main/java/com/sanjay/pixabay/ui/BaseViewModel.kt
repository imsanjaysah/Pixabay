/*
 * BaseViewModel.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    abstract var disposable : CompositeDisposable

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}