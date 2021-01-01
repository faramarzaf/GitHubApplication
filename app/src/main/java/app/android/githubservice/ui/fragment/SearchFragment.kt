package app.android.githubservice.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.entity.search.Item
import app.android.githubservice.model.db.GitHubDatabase
import app.android.githubservice.model.network.RetrofitInstance
import app.android.githubservice.repository.Resource
import app.android.githubservice.repository.SearchRepository
import app.android.githubservice.ui.adapter.SearchAdapter
import app.android.githubservice.util.MAX_PAGE
import app.android.githubservice.util.MIN_PAGE
import app.android.githubservice.viewmodel.SearchViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_list_searched_users.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {

    lateinit var viewModel: SearchViewModel
    lateinit var searchAdapter: SearchAdapter
    private lateinit var user: Item

    override val getFragmentLayout: Int
        get() = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupRecyclerView()
        getUsersList()
        searchAdapter.getItemInstance {
            searchAdapter.getViewFromAdapter { views ->
                if (!viewModel.userExists(it)) {
                    views.setColorFilter(Color.WHITE)
                } else
                    views.setColorFilter(Color.RED)
            }
        }
        handleSearchRepositoryData()
        searchAdapter.setOnItemClickListener {
            toast(it.login)
        }


        searchAdapter.setOnSaveUserClickListener {
            if (!viewModel.userExists(it)) {
                imageFav.setColorFilter(Color.RED)
                saveUser(it)
            } else if (viewModel.userExists(it)) {
                imageFav.setColorFilter(Color.RED)
                toast("user already in favorite")
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

    private fun handleSearchRepositoryData() {
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
                    toast(response.toString())
                }
            }
        })
    }

    private fun initViewModel() {
        val factory = ViewModelFactory(SearchRepository(RetrofitInstance.api, GitHubDatabase(requireActivity())))
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

    }

    private fun saveUser(user: Item) {
        viewModel.saveUser(user)
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()
        rv_search.apply {
            adapter = searchAdapter
            setItemViewCacheSize(500)
            layoutManager = LinearLayoutManager(activity)
        }
    }


}