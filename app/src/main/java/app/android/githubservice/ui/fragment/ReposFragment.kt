package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.network.RetrofitInstance
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.ui.adapter.ReposAdapter
import app.android.githubservice.util.KEY_USERNAME
import app.android.githubservice.viewmodel.RepositoriesViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_repos.*


class ReposFragment : BaseFragment() {

    lateinit var viewModel: RepositoriesViewModel
    lateinit var reposAdapter: ReposAdapter

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_repos
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       // initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*        initViewModel()
        callWebService(view)
        handleResponse()
        setupRecyclerView()
        reposAdapter.setOnItemClickListener {
            Toast.makeText(activity, it.name, Toast.LENGTH_SHORT).show()
        }*/
    }

    fun initViewModel() {
        val factory = ViewModelFactory(ReposRepository(RetrofitInstance().getApi()))
        viewModel = ViewModelProvider(this, factory).get(RepositoriesViewModel::class.java)
    }

    fun callWebService(view: View) {
        viewModel.getRepos(MyPreferences.readString(view.context, KEY_USERNAME, "faramarz"))
    }

    fun handleResponse() {
        viewModel.reposResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    for (i in response.value.repos) {
                        Log.d("TAG00Results", " RESPONSE:         " + i.name)
                    }
                }
                is Resource.Failure -> {

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
            // addOnScrollListener(this@ReposFragment.scrollListener)
        }
    }


}