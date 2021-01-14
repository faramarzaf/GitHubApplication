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
import app.android.githubservice.ui.adapter.StarredAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.StarredViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_starred.*

@AndroidEntryPoint
class StarredFragment : BaseFragment() {


    private lateinit var starredAdapter: StarredAdapter

    private val viewModel: StarredViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_starred

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStarredRepositories(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
        setupRecyclerView()
        fetchStarredRepositoryData()
        starredAdapter.setOnItemClickListener {
            toast(it.name)
        }
    }

    private fun fetchStarredRepositoryData() {
        showProgressBar()
        viewModel.starredResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    starredAdapter.differ.submitList(response.value)
                    rv_starred.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "fetchStarredRepositoryData: $response")
                }
            }
        })
    }

    private fun hideProgressBar() {
        starredProgressBar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        starredProgressBar.visibility = View.VISIBLE

    }

    private fun setupRecyclerView() {
        starredAdapter = StarredAdapter()
        rv_starred.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = starredAdapter
            layoutManager = LinearLayoutManager(context)

        }
    }

}