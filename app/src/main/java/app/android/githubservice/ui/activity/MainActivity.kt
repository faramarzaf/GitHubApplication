package app.android.githubservice.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import app.android.githubservice.R
import app.android.githubservice.base.BaseActivity
import app.android.githubservice.network.RetrofitInstance
import app.android.githubservice.repository.BaseRepository
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.util.KEY_USERNAME
import app.android.githubservice.viewmodel.RepositoriesViewModel
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<RepositoriesViewModel>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
        handleAuthResponse()
        viewModel.getRepos(MyPreferences.readString(this, KEY_USERNAME, "faramarz"))
    }


    fun handleAuthResponse() {
        viewModel.reposResponse.observe(this, Observer { response ->
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




    override fun getRepository(): BaseRepository {
        return ReposRepository(RetrofitInstance().getApi())
    }

    override fun getViewModel()=
        RepositoriesViewModel::class.java
}