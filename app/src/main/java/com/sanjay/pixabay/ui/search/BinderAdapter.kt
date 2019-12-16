package com.sanjay.pixabay.ui.search

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sanjay.pixabay.R
import com.squareup.picasso.Picasso

@BindingAdapter("android:src")
fun loadImage(img: ImageView, imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .placeholder(R.drawable.ic_placeholder)
        .into(img)
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}