package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.di.AppModule
import app.android.githubservice.model.db.GitHubDatabase
import app.android.githubservice.model.network.GitHubApi
import app.android.githubservice.repository.Resource
import app.android.githubservice.repository.StarredRepository
import app.android.githubservice.ui.adapter.StarredAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.StarredViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_starred.*
import javax.inject.Inject

@AndroidEntryPoint
class StarredFragment : BaseFragment() {

    lateinit var viewModel: StarredViewModel
    lateinit var starredAdapter: StarredAdapter

    @Inject
    lateinit var api: GitHubApi

    override val getFragmentLayout: Int
        get() = R.layout.fragment_starred

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(StarredRepository(api))
        viewModel = ViewModelProvider(this, factory).get(StarredViewModel::class.java)
        viewModel.getStarredRepositories(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
        setupRecyclerView()
        fetchStarredRepositoryData()
        starredAdapter.setOnItemClickListener {
            toast(it.name)
        }
    }

    private fun fetchStarredRepositoryData() {
        showProgressBar()
        viewModel.starredResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    starredAdapter.differ.submitList(response.value)
                    rv_starred.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "fetchStarredRepositoryData: $response")
                }
            }
        })
    }

    private fun hideProgressBar() {
        starredProgressBar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        starredProgressBar.visibility = View.VISIBLE

    }

    private fun setupRecyclerView() {
        starredAdapter = StarredAdapter()
        rv_starred.apply {
            adapter = starredAdapter
            setItemViewCacheSize(500)
            layoutManager = LinearLayoutManager(context)

        }
    }

}