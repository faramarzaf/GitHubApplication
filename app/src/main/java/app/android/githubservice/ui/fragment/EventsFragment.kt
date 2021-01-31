package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.databinding.FragmentEventsBinding
import app.android.githubservice.ui.adapter.EventsAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.EventsViewModel
import app.android.githubservice.viewmodel.FollowersViewModel
import app.android.githubservice.viewmodel.FollowingViewModel
import app.android.githubservice.viewmodel.RepositoriesViewModel
import com.faramarzaf.sdk.af_android_sdk.core.helper.IntentHelper
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsFragment : BaseFragment() {

    private lateinit var binding: FragmentEventsBinding
    private lateinit var eventsAdapter: EventsAdapter
    private val viewModel: EventsViewModel by viewModels()
    private val viewModelRepositories: RepositoriesViewModel by viewModels()
    private val viewModelFollowers: FollowersViewModel by viewModels()
    private val viewModelFollowing: FollowingViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_events

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventsBinding.bind(view)
        getEvents()
        getRepos()
        getFollowers()
        getFollowing()
        observeEventsList()
        observeRepositoryData()
        observeFollowers()
        observeFollowing()
        setupRecyclerView()
        eventsAdapter.setOnRepoClickListener {
            IntentHelper.openUrl(requireContext(), it)
        }
    }


    private fun getEvents() {
        viewModel.getEvents()
    }

    private fun observeEventsList() {
        showProgressBar(binding.eventsProgressBar)
        viewModel.eventsResponse.observe(viewLifecycleOwner, Observer { eventsResponse ->
            when (eventsResponse) {
                is Resource.Success -> {
                    hideProgressBar(binding.eventsProgressBar)
                    eventsAdapter.differ.submitList(eventsResponse.value)
                    binding.rvEvents.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar(binding.eventsProgressBar)
                    if (eventsResponse.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "getFollowersAsync: $eventsResponse")
                }
            }
        })
    }

    /**
     * Fetch followers, following and repositories data for store in shared pref and show them in profile page
     */

    private fun getRepos() {
        viewModelRepositories.getRepos(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
    }

    private fun getFollowers() {
        viewModelFollowers.getFollowers(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
    }

    private fun getFollowing() {
        viewModelFollowing.getFollowing(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
    }


    private fun observeRepositoryData() {
        viewModelRepositories.reposResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.value.isEmpty()) {
                        Log.d(TAG_LOG, "observeRepositoryData: response.value.isEmpty")
                    } else {
                        MyPreferences.writeString(requireContext(), KEY_SIZE_LIST_REPO, response.value.size.toString())
                    }
                }
                is Resource.Failure -> {
                    if (response.isNetworkError) {
                        Log.d(TAG_LOG, "observeRepositoryData: Check your connection!")
                    }
                    Log.d(TAG_LOG, "fetchRepositoryData: $response")
                }
            }
        })
    }

    private fun observeFollowers() {
        viewModelFollowers.followersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    MyPreferences.writeString(requireContext(), KEY_NUMBER_FOLLOWERS, response.value.size.toString())
                }
                is Resource.Failure -> {
                    if (response.isNetworkError) {
                        Log.d(TAG_LOG, "observeFollowers: Check your connection!")
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
                        Log.d(TAG_LOG, "observeFollowing: Check your connection!")
                    }
                    Log.d(TAG_LOG, "getFollowingAsync: $response")
                }
            }
        })
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter()
        binding.rvEvents.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(context)
            setItemViewCacheSize(CACHE_SIZE)
        }
    }

}