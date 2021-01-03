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
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.adapter.FollowersFollowingAdapter
import app.android.githubservice.util.DEFAULT_USER
import app.android.githubservice.util.KEY_USERNAME
import app.android.githubservice.util.MAX_PAGE
import app.android.githubservice.util.MIN_PAGE
import app.android.githubservice.viewmodel.FollowersViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : BaseFragment() {

    private lateinit var viewModel: FollowersViewModel
    private lateinit var followersAdapter: FollowersFollowingAdapter

    override val getFragmentLayout: Int
        get() = R.layout.fragment_followers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getFollowers()
        handleFollowersRepositoryData()
        setupRecyclerView()
        followersAdapter.setOnItemClickListener {
            toast(it.login)
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory(FollowersRepository(RetrofitInstance.api))
        viewModel = ViewModelProvider(this, factory).get(FollowersViewModel::class.java)

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
                    toast(response.toString())
                }
            }
        })
    }

    private fun setupRecyclerView() {
        followersAdapter = FollowersFollowingAdapter()
        rv_followers.apply {
            adapter = followersAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}

