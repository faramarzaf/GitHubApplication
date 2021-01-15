package app.android.githubservice.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.entity.search.Item
import app.android.githubservice.util.Resource
import app.android.githubservice.ui.adapter.SearchAdapter
import app.android.githubservice.util.MAX_PAGE
import app.android.githubservice.util.MIN_PAGE
import app.android.githubservice.util.TAG_LOG
import app.android.githubservice.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUsersList()
        setupRecyclerView()
        observeSearchRepositoryData()
        favoriteUserOperation()
    }

    private fun favoriteUserOperation() {
        with(searchAdapter) {
            getItemInstance {
                getViewFromAdapter { views ->
                    if (!viewModel.userExists(it)) {
                        views.setColorFilter(Color.WHITE)
                    } else
                        views.setColorFilter(Color.RED)
                }
            }
            setOnItemClickListener {
                toast(it.login)
            }
            setOnSaveUserClickListener { item, imageview ->

                if (!viewModel.userExists(item)) {
                    imageview.setColorFilter(Color.RED)
                    saveUser(item)
                } else if (viewModel.userExists(item)) {
                    imageview.setColorFilter(Color.RED)
                    toast("User is already in favorites!")
                }
            }
        }
    }

    private fun getUsersList() {
        var job: Job? = null
        editTextSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                showProgressBar(searchProgressBar)
                delay(500)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchUser(editable.toString(), MIN_PAGE, MAX_PAGE)
                    } else {
                        hideProgressBar(searchProgressBar)
                    }
                }
            }
        }
    }

    private fun observeSearchRepositoryData() {
        viewModel.searchResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(searchProgressBar)
                    searchAdapter.differ.submitList(response.value.items)
                    rv_search.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar(searchProgressBar)
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "handleSearchRepositoryData: $response")
                }
            }
        })
    }

    private fun saveUser(user: Item) {
        viewModel.saveUser(user)
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()
        rv_search.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}