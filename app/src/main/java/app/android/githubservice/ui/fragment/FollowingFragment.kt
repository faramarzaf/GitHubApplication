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
import app.android.githubservice.ui.adapter.FollowersFollowingAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.FollowingViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_following.*

@AndroidEntryPoint
class FollowingFragment : BaseFragment() {

    private lateinit var followingAdapter: FollowersFollowingAdapter
    private val viewModel: FollowingViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_following

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFollowing()
        setupRecyclerView()
        handleFollowingRepositoryData()
        followingAdapter.setOnItemClickListener {
            toast(it.login)
        }
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
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}