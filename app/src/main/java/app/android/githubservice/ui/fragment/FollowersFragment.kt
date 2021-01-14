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
import app.android.githubservice.viewmodel.FollowersViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_followers.*

@AndroidEntryPoint
class FollowersFragment : BaseFragment() {

    private lateinit var followersAdapter: FollowersFollowingAdapter

    private val viewModel: FollowersViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_followers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFollowers()
        handleFollowersRepositoryData()
        setupRecyclerView()
        followersAdapter.setOnItemClickListener {
            toast(it.login)
        }
    }

    private fun getFollowers() {
        viewModel.getFollowers(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
    }

    private fun handleFollowersRepositoryData() {
        showProgressBar(followersProgressBar)
        viewModel.followersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(followersProgressBar)
                    followersAdapter.differ.submitList(response.value)
                    rv_followers.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar(followersProgressBar)
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "handleFollowersRepositoryData: $response")
                }
            }
        })
    }

    private fun setupRecyclerView() {
        followersAdapter = FollowersFollowingAdapter()
        rv_followers.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = followersAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}

