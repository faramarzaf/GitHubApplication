package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.FollowersViewModel
import app.android.githubservice.viewmodel.FollowingViewModel
import app.android.githubservice.viewmodel.RepositoriesViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_repos.*

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
        observeFollowers()
        observeFollowing()
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

    private fun observeFollowers() {
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

    private fun observeFollowing() {
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