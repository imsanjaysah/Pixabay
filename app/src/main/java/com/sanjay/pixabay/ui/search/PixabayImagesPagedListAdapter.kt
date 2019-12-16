/*
 * Created by Sanjay.Sah
 */

package com.sanjay.pixabay.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.pixabay.R
import com.sanjay.pixabay.constants.State
import com.sanjay.pixabay.data.repository.remote.model.PixabayImage
import com.sanjay.pixabay.databinding.ItemListPixabayImageBinding
import com.sanjay.pixabay.ui.view.ListFooterViewHolder
import kotlinx.android.synthetic.main.item_list_pixabay_image.view.*


/**
 * Adapter responsible for binding the list of ingredients to RecyclerView
 */
class PixabayImagesPagedListAdapter(
    /*private val onItemClick: (PixabayImage, ImageView) -> Unit,
    private val retry: () -> Unit*/
) : PagedListAdapter<PixabayImage, RecyclerView.ViewHolder>(diffCallback) {


    private var state = State.LOADING

    companion object {
        const val DATA_VIEW_TYPE = 1
        const val FOOTER_VIEW_TYPE = 2
        /**
         * DiffUtils is used improve the performance by finding difference between two lists and updating only the new items
         */
        private val diffCallback = object : DiffUtil.ItemCallback<PixabayImage>() {
            override fun areItemsTheSame(oldItem: PixabayImage, newItem: PixabayImage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PixabayImage, newItem: PixabayImage): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) PixabayImageViewHolder.create(parent) else ListFooterViewHolder.create(
            {

            },
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as PixabayImageViewHolder).bind(getItem(position)!!)
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}

/**
 * ViewHolder to display ingredient information
 */
class PixabayImageViewHolder(val binding: ItemListPixabayImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.setClickListener {
            binding.pixabayImage?.let { image ->
                navigateToImageDetail(image, it)
            }
        }
    }

    private fun navigateToImageDetail(image: PixabayImage, view: View) {
        val direction = PixabayImagesFragmentDirections.actionImageDetail(image)
        view.findNavController().navigate(direction)
    }

    fun bind(item: PixabayImage) {
        binding.apply {
            pixabayImage = item
            executePendingBindings()
        }
        binding.tagGroup.setTags(item.getTags())

    }

    companion object {
        fun create(parent: ViewGroup): PixabayImageViewHolder {

            val binding = DataBindingUtil.inflate<ItemListPixabayImageBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_list_pixabay_image,
                parent,
                false
            )
            return PixabayImageViewHolder(binding)
        }
    }
}
