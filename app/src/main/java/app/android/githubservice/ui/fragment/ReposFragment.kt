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
import app.android.githubservice.repository.SearchRepository
import app.android.githubservice.ui.activity.MainActivity
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.DEFAULT_USER
import app.android.githubservice.util.KEY_USERNAME
import app.android.githubservice.util.MAX_PAGE
import app.android.githubservice.util.MIN_PAGE
import app.android.githubservice.viewmodel.RepositoriesViewModel
import app.android.githubservice.viewmodel.SearchViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_repos.*


class ReposFragment : BaseFragment() {

    lateinit var viewModel: RepositoriesViewModel
    lateinit var reposAdapter: ReposAdapter

    override val getFragmentLayout: Int
        get() = R.layout.fragment_repos

    override fun newInstance(): Fragment {
        return ReposFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = (activity as MainActivity).viewModel

        val factory = ViewModelFactory(ReposRepository(RetrofitInstance.api))
        viewModel = ViewModelProvider(this, factory).get(RepositoriesViewModel::class.java)
        viewModel.getRepos(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
        setupRecyclerView()
        fetchRepositoryData()
        reposAdapter.setOnItemClickListener {
            toast(it.name)
        }
    }

    private fun fetchRepositoryData() {
        showProgressBar(reposProgressBar)
        viewModel.reposResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(reposProgressBar)
                    reposAdapter.differ.submitList(response.value)
                    rv_repos.setPadding(0, 0, 0, 0)
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
            layoutManager = LinearLayoutManager(activity)

        }
    }

}