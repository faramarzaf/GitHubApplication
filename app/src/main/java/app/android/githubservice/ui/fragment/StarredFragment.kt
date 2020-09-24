package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.model.network.RetrofitInstance
import app.android.githubservice.repository.BaseRepository
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.repository.StarredRepository
import app.android.githubservice.ui.adapter.StarredAdapter
import app.android.githubservice.util.DEFAULT_USER
import app.android.githubservice.util.KEY_USERNAME
import app.android.githubservice.util.MAX_PAGE
import app.android.githubservice.util.MIN_PAGE
import app.android.githubservice.viewmodel.RepositoriesViewModel
import app.android.githubservice.viewmodel.StarredViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_starred.*

class StarredFragment : BaseFragment() {

    lateinit var viewModel: StarredViewModel
    lateinit var starredAdapter: StarredAdapter


    override val getFragmentLayout: Int
        get() = R.layout.fragment_starred


    override fun newInstance(): Fragment {
        return StarredFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(StarredRepository(RetrofitInstance.api))
        viewModel = ViewModelProvider(this, factory).get(StarredViewModel::class.java)
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
                    toast(response.toString())
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
            adapter = starredAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

}