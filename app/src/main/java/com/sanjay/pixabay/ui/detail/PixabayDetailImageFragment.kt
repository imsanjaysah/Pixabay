package com.sanjay.pixabay.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sanjay.pixabay.R
import com.sanjay.pixabay.databinding.FragmentImageDetailBinding
import com.sanjay.pixabay.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_image_detail.*
import javax.inject.Inject

class PixabayDetailImageFragment : BaseFragment() {

    private val args: PixabayDetailImageFragmentArgs by navArgs()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var imageDetailViewModel: PixabayImageDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        imageDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(PixabayImageDetailViewModel::class.java)
        imageDetailViewModel.image = args.image

        val binding = DataBindingUtil.inflate<FragmentImageDetailBinding>(
            inflater,
            R.layout.fragment_image_detail,
            container,
            false
        ).apply {
            viewModel = imageDetailViewModel
            lifecycleOwner = viewLifecycleOwner

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            tagGroup.setTags(args.image.getTags())
        }
        return binding.root
    }

}