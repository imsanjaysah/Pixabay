package com.sanjay.pixabay.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanjay.pixabay.data.repository.PixabayRepository
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import javax.inject.Inject

class PixabayImageDetailViewModel @Inject constructor(repository: PixabayRepository) : ViewModel() {

    var image: PixabayImage? = null
}