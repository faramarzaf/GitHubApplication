package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.databinding.FragmentStarredBinding
import app.android.githubservice.ui.adapter.StarredAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.StarredViewModel
import com.faramarzaf.sdk.af_android_sdk.core.helper.IntentHelper
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_starred.*

@AndroidEntryPoint
class StarredFragment : BaseFragment() {

    private lateinit var starredAdapter: StarredAdapter
    private lateinit var binding: FragmentStarredBinding
    private val viewModel: StarredViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_starred

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStarredBinding.bind(view)
        viewModel.getStarredRepositories(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
        setupRecyclerView()
        observeStarredRepositoryData()
        starredAdapter.setOnRepoClickListener {
            IntentHelper.openUrl(requireContext(), it)
        }
    }

    private fun observeStarredRepositoryData() {
        showProgressBar(binding.starredProgressBar)
        viewModel.starredResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.value.isEmpty()) {
                        noDataAvailable()
                    } else {
                        dataAvailable()
                    }
                    hideProgressBar(binding.starredProgressBar)
                    starredAdapter.differ.submitList(response.value)
                    rvStarred.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar(binding.starredProgressBar)
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "fetchStarredRepositoryData: $response")
                }
            }
        })
    }


    private fun setupRecyclerView() {
        starredAdapter = StarredAdapter()
        binding.rvStarred.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = starredAdapter
            layoutManager = LinearLayoutManager(context)
            setItemViewCacheSize(CACHE_SIZE)
        }
    }

    private fun noDataAvailable() {
        binding.textNoStarred.visibility = View.VISIBLE
    }

    private fun dataAvailable() {
        binding.textNoStarred.visibility = View.GONE
    }

}