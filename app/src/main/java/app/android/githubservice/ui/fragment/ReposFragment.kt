package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.model.network.RetrofitInstance
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.RepositoriesViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_repos.*


class ReposFragment : BaseFragment() {

    private lateinit var viewModel: RepositoriesViewModel
    private lateinit var reposAdapter: ReposAdapter

    override val getFragmentLayout: Int
        get() = R.layout.fragment_repos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.getRepos(MyPreferences.readString(requireActivity(), KEY_USERNAME, DEFAULT_USER), MIN_PAGE, MAX_PAGE)
        setupRecyclerView()
        fetchRepositoryData()
        reposAdapter.setOnItemClickListener {
            toast(it.name)
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory(ReposRepository(RetrofitInstance.api))
        viewModel = ViewModelProvider(this, factory).get(RepositoriesViewModel::class.java)
    }

    private fun fetchRepositoryData() {
        showProgressBar(reposProgressBar)
        viewModel.reposResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(reposProgressBar)
                    reposAdapter.differ.submitList(response.value)
                    rv_repos.setPadding(0, 0, 0, 0)
                    MyPreferences.writeString(
                        requireContext(), KEY_SIZE_LIST_REPO,
                        reposAdapter.itemCount.toString()
                    )
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
            setItemViewCacheSize(500)
            layoutManager = LinearLayoutManager(context)
        }
    }

}