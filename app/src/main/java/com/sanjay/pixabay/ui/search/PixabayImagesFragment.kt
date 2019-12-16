package com.sanjay.pixabay.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.sanjay.pixabay.R
import com.sanjay.pixabay.constants.State
import com.sanjay.pixabay.databinding.FragmentPixabayImagesBinding
import com.sanjay.pixabay.ui.BaseFragment
import com.sanjay.pixabay.ui.search.PixabayImagesPagedListAdapter.Companion.DATA_VIEW_TYPE
import com.sanjay.pixabay.ui.search.PixabayImagesPagedListAdapter.Companion.FOOTER_VIEW_TYPE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PixabayImagesFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PixabaySearchViewModel
    private lateinit var pixabayAdapter: PixabayImagesPagedListAdapter

    private var binding: FragmentPixabayImagesBinding? = null
    private var searchView: SearchView? = null
    private var searchSubscription: Disposable? = null

    private var isQueryChanged: Boolean = false
    private var isPaused = false
    private var isSearchViewOpened = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*  if (binding != null) {
              return binding?.root
          }*/
        if (binding == null) {
            binding = FragmentPixabayImagesBinding.inflate(inflater, container, false)
            context ?: return binding?.root
            viewModel =
                ViewModelProvider(this, viewModelFactory).get(PixabaySearchViewModel::class.java)
            viewModel.searchQuery.value = DEFAULT_SEARCH

        }
        initAdapter()
        initState()
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        setHasOptionsMenu(true)
        return binding?.root
    }


    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        pixabayAdapter = PixabayImagesPagedListAdapter()

        (binding?.recyclerViewImages?.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (pixabayAdapter.getItemViewType(position)) {
                        DATA_VIEW_TYPE -> 1
                        FOOTER_VIEW_TYPE -> 2 //number of columns of the grid
                        else -> -1
                    }
                }
            }
        //set the adapter
        binding?.recyclerViewImages?.adapter = pixabayAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.images?.observe(viewLifecycleOwner, Observer {
            pixabayAdapter.submitList(it)
            //Workaround to fix this issue
            //https://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position
            if (isQueryChanged) {
                pixabayAdapter.notifyDataSetChanged()
                isQueryChanged = false
            }
        })
    }

    /**
     * Initializing the state
     */
    private fun initState() {
        //On click of retry textview call the api again
        binding?.txtError?.setOnClickListener { viewModel.retry() }
        //Observing the different states of the API calling, and updating the UI accordingly
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            binding?.progressBar?.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            binding?.txtError?.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                pixabayAdapter.setState(state ?: State.DONE)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        isSearchViewOpened = searchView!!.isIconified == false

    }

    override fun onResume() {
        super.onResume()
        isPaused = false


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_activity_pixabay, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView!!.queryHint = "Search fruits, flowers, etc."
        if (isSearchViewOpened) {
            searchItem.expandActionView()
            searchView?.setQuery(viewModel.searchQuery.value, false)
        }

        searchSubscription = searchView!!.queryTextChanges().skip(1)
            .debounce(
                500,
                TimeUnit.MILLISECONDS
            )//deferring the event for sometime as it is expected that user would type in a fast way
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (!isPaused) {
                    isQueryChanged = true
                    viewModel.searchQuery.value =
                        if (it.toString().isEmpty()) DEFAULT_SEARCH else it.toString()
                }
            }
    }

    companion object {
        const val DEFAULT_SEARCH = "fruits"
    }
}