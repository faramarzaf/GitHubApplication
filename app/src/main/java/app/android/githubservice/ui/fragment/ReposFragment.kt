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
import app.android.githubservice.databinding.FragmentReposBinding
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.RepositoriesViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_repos.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReposFragment : BaseFragment() {


    private lateinit var reposAdapter: ReposAdapter
    private lateinit var binding: FragmentReposBinding
    private val viewModel: RepositoriesViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_repos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReposBinding.bind(view)
        callSuspendFunctions()
        observeRepositoryData()
        setupRecyclerView()
        reposAdapter.setOnItemClickListener {
            toast(it.name)
        }
    }

    private fun callSuspendFunctions() {
        lifecycleScope.launch {
            getRepos()
        }
    }

    private fun setupRecyclerView() {
        reposAdapter = ReposAdapter()
        binding.rvRepos.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = reposAdapter
            layoutManager = LinearLayoutManager(context)
            setItemViewCacheSize(CACHE_SIZE)
        }
    }

    private suspend fun getRepos() {
        viewModel.getRepos(MyDataStore(requireContext()).readString(KEY_USERNAME).toString(), MIN_PAGE, MAX_PAGE)
    }


    private fun observeRepositoryData() {
        showProgressBar(binding.reposProgressBar)
        viewModel.reposResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.value.isEmpty()) {
                        noDataAvailable()
                    } else {
                        dataAvailable()
                    }
                    hideProgressBar(binding.reposProgressBar)
                    reposAdapter.differ.submitList(response.value)
                    rvRepos.setPadding(0, 0, 0, 0)
                }
                is Resource.Failure -> {
                    hideProgressBar(binding.reposProgressBar)
                    if (response.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "fetchRepositoryData: $response")
                }
            }
        })
    }

    private fun noDataAvailable() {
        binding.textNoRepos.visibility = View.VISIBLE
    }

    private fun dataAvailable() {
        binding.textNoRepos.visibility = View.GONE
    }

}