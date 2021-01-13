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
import app.android.githubservice.model.network.GitHubApi
import app.android.githubservice.repository.FollowingRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.adapter.FollowersFollowingAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.FollowingViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_following.*
import javax.inject.Inject

@AndroidEntryPoint
class FollowingFragment : BaseFragment() {

    private lateinit var viewModel: FollowingViewModel
    private lateinit var followingAdapter: FollowersFollowingAdapter

    @Inject
    lateinit var api: GitHubApi

    override val getFragmentLayout: Int
        get() = R.layout.fragment_following

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getFollowing()
        setupRecyclerView()
        handleFollowingRepositoryData()
        followingAdapter.setOnItemClickListener {
            toast(it.login)
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory(FollowingRepository(api))
        viewModel = ViewModelProvider(this, factory).get(FollowingViewModel::class.java)

    }

    private fun getFollowing() {
        viewModel.getFollowing(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
    }

    private fun handleFollowingRepositoryData() {
        showProgressBar(followingProgressBar)
        viewModel.followingResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(followingProgressBar)
                    followingAdapter.differ.submitList(response.value)
                    rv_following.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar(followingProgressBar)
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "handleFollowingRepositoryData: $response")
                }
            }
        })
    }

    private fun setupRecyclerView() {
        followingAdapter = FollowersFollowingAdapter()
        rv_following.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}