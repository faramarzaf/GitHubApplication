package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.activity.MainActivity
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.KEY_USERNAME
import app.android.githubservice.viewmodel.RepositoriesViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_repos.*


class ReposFragment : BaseFragment() {

    lateinit var viewModel: RepositoriesViewModel
    lateinit var reposAdapter: ReposAdapter

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_repos
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        viewModel.getRepos(MyPreferences.readString(requireActivity(), KEY_USERNAME, "faramarzaf"), 1, 1000)
        fetchRepositoryData()
        reposAdapter.setOnItemClickListener {
            toast(it.name)
        }
    }


    private fun fetchRepositoryData() {
        showProgressBar()
        viewModel.reposResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    reposAdapter.differ.submitList(response.value)
                    rv_repos.setPadding(0, 0, 0, 0)
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
        paginationProgressBar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE

    }

    private fun setupRecyclerView() {
        reposAdapter = ReposAdapter()
        rv_repos.apply {
            adapter = reposAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }


}