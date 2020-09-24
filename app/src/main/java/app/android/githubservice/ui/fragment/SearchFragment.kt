package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.model.network.RetrofitInstance
import app.android.githubservice.repository.BaseRepository
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.repository.SearchRepository
import app.android.githubservice.ui.adapter.SearchAdapter
import app.android.githubservice.util.MAX_PAGE
import app.android.githubservice.util.MIN_PAGE
import app.android.githubservice.viewmodel.RepositoriesViewModel
import app.android.githubservice.viewmodel.SearchViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {

    lateinit var viewModel: SearchViewModel
    lateinit var searchAdapter: SearchAdapter

    override fun newInstance(): Fragment {
        return SearchFragment()
    }

    override val getFragmentLayout: Int
        get() = R.layout.fragment_search



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(SearchRepository(RetrofitInstance.api))
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
        setupRecyclerView()
        getUsersList()
        fetchSearchRepositoryData()
        searchAdapter.setOnItemClickListener {
            toast(it.login)
        }
    }

    private fun getUsersList() {
        var job: Job? = null
        editTextSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = viewLifecycleOwner.lifecycle.coroutineScope.launch {
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

    private fun fetchSearchRepositoryData() {
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

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()
        rv_search.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}