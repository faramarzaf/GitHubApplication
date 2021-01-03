package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.model.network.RetrofitInstance
import app.android.githubservice.repository.FollowersRepository
import app.android.githubservice.repository.FollowingRepository
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.FollowersViewModel
import app.android.githubservice.viewmodel.FollowingViewModel
import app.android.githubservice.viewmodel.RepositoriesViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_repos.*


class ReposFragment : BaseFragment() {

    private lateinit var viewModel: RepositoriesViewModel
    private lateinit var viewModelFollowers: FollowersViewModel
    private lateinit var viewModelFollowing: FollowingViewModel
    private lateinit var reposAdapter: ReposAdapter

    override val getFragmentLayout: Int
        get() = R.layout.fragment_repos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
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

    private fun initViewModel() {
        val factory = ViewModelFactory(ReposRepository(RetrofitInstance.api))
        val factoryFollower = ViewModelFactory(FollowersRepository(RetrofitInstance.api))
        val factoryFollowing = ViewModelFactory(FollowingRepository(RetrofitInstance.api))
        viewModel = ViewModelProvider(this, factory).get(RepositoriesViewModel::class.java)
        viewModelFollowers = ViewModelProvider(this, factoryFollower).get(FollowersViewModel::class.java)
        viewModelFollowing = ViewModelProvider(this, factoryFollowing).get(FollowingViewModel::class.java)
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
                    toast(response.toString())
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
                    toast(response.toString())
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
                    toast(response.toString())
                }
            }
        })
    }
}