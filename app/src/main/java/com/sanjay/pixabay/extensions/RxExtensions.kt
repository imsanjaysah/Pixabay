/*
 * RxExtensions.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.extensions

import android.text.TextUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.regex.Pattern

/**
 * @author Sanjay.Sah
 */

fun Disposable.addToCompositeDisposable(composite: CompositeDisposable) {
    composite.add(this)
}

fun String?.notEmpty(): Boolean =
    !TextUtils.isEmpty(this?.trim())
