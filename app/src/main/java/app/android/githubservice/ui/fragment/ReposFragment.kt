package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.model.network.GitHubApi
import app.android.githubservice.repository.FollowersRepository
import app.android.githubservice.repository.FollowingRepository
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.*
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_repos.*
import javax.inject.Inject

@AndroidEntryPoint
class ReposFragment : BaseFragment() {


    private lateinit var reposAdapter: ReposAdapter

    private val viewModel: RepositoriesViewModel by viewModels()
    private val viewModelFollowers: FollowersViewModel by viewModels()
    private val viewModelFollowing: FollowingViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_repos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRepos(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
        setupRecyclerView()
        fetchRepositoryData()
        getFollowers()
        getFollowing()
        getFollowersAsync()
        getFollowingAsync()
        reposAdapter.setOnItemClickListener {
            toast(it.name)
        }
    }


    private fun fetchRepositoryData() {
        showProgressBar(reposProgressBar)
        viewModel.reposResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(reposProgressBar)
                    reposAdapter.differ.submitList(response.value)
                    rv_repos.setPadding(0, 0, 0, 0)
                    MyPreferences.writeString(requireContext(), KEY_SIZE_LIST_REPO, response.value.size.toString())
                }
                is Resource.Failure -> {
                    hideProgressBar(reposProgressBar)
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "fetchRepositoryData: $response")
                }
            }
        })
    }


    private fun setupRecyclerView() {
        reposAdapter = ReposAdapter()
        rv_repos.apply {
            adapter = reposAdapter
            setItemViewCacheSize(500)
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Fetch followers and following data for store in share pref and show them in profile page
     */
    private fun getFollowers() {
        viewModelFollowers.getFollowers(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
    }

    private fun getFollowing() {
        viewModelFollowing.getFollowing(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)

    }

    private fun getFollowersAsync() {
        viewModelFollowers.followersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    MyPreferences.writeString(requireContext(), KEY_NUMBER_FOLLOWERS, response.value.size.toString())
                }
                is Resource.Failure -> {
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "getFollowersAsync: $response")
                }
            }
        })
    }

    private fun getFollowingAsync() {
        viewModelFollowing.followingResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    MyPreferences.writeString(requireContext(), KEY_NUMBER_FOLLOWING, response.value.size.toString())
                }
                is Resource.Failure -> {
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "getFollowingAsync: $response")
                }
            }
        })
    }
}