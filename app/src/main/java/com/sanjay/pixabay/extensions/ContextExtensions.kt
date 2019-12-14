/*
 * ContextExtensions.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.extensions

import android.net.ConnectivityManager

/**
 * @author Sanjay.Sah
 */

val ConnectivityManager.isConnected: Boolean
    get() = activeNetworkInfo?.isConnected ?: false