package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.databinding.FragmentFollowersBinding
import app.android.githubservice.ui.adapter.FollowersFollowingAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.FollowersViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowersFragment : BaseFragment() {

    private lateinit var followersAdapter: FollowersFollowingAdapter
    private lateinit var binding: FragmentFollowersBinding
    private val viewModel: FollowersViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_followers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowersBinding.bind(view)
        callSuspendFunctions()
        observeFollowersRepositoryData()
        setupRecyclerView()
        followersAdapter.setOnItemClickListener {
            toast(it.login)
        }
    }

    private fun callSuspendFunctions() {
        lifecycleScope.launch {
            getFollowers()
        }
    }

    private suspend fun getFollowers() {
        viewModel.getFollowers(MyDataStore(requireContext()).readString(KEY_USERNAME).toString(), MIN_PAGE, MAX_PAGE)

    }

    private fun observeFollowersRepositoryData() {
        showProgressBar(binding.followersProgressBar)
        viewModel.followersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.value.isEmpty()) {
                        noDataAvailable()
                    } else {
                        dataAvailable()
                    }
                    hideProgressBar(binding.followersProgressBar)
                    followersAdapter.differ.submitList(response.value)
                    binding.rvFollowers.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar(binding.followersProgressBar)
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
        binding.rvFollowers.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = followersAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

    private fun noDataAvailable() {
        binding.textNoFollowers.visibility = View.VISIBLE
    }

    private fun dataAvailable() {
        binding.textNoFollowers.visibility = View.GONE
    }
}