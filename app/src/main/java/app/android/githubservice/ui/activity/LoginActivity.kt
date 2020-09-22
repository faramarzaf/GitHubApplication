package app.android.githubservice.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import app.android.githubservice.R
import app.android.githubservice.base.BaseActivity
import app.android.githubservice.model.search.SearchResponse
import app.android.githubservice.network.RetrofitInstance
import app.android.githubservice.repository.AuthRepository
import app.android.githubservice.repository.BaseRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.AuthViewModel
import com.faramarzaf.sdk.af_android_sdk.core.helper.StringHelper
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<AuthViewModel>(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(this)
        handleAuthResponse()
        checkUserIsAuth()
    }

    override fun onClick(v: View?) {
        showProgressBar(authProgressBar)
        if (editTextUsername.text.toString().trim().isNotEmpty() && editTextPassword.text.toString().trim().isNotEmpty()) {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            MyPreferences.writeString(this, KEY_USERNAME, username)
            MyPreferences.writeString(this, KEY_PASSWORD, password)
            viewModel.auth(username)
        } else if (StringHelper.stringIsEmptyOrNull(editTextUsername.text.toString().trim()) &&
            StringHelper.stringIsEmptyOrNull(editTextPassword.text.toString().trim())
        ) {
            hideProgressBar(authProgressBar)
            toast("Fill fields!")
        }
    }

    private fun checkUserIsAuth() {
        if (MyPreferences.readBoolean(this, KEY_IS_LOGGED_IN, false)) {
            finish()
            toActivity(MainActivity::class.java)
        } else
            return
    }

    private fun handleAuthResponse() {
        viewModel.loginResponse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar(authProgressBar)
                    if (it.value.totalCount == 0) {
                        toast("User not found!")
                    } else {
                        hideProgressBar(authProgressBar)
                        MyPreferences.writeBoolean(this, KEY_IS_LOGGED_IN, true)
                        saveUsefulUrls(it.value)
                        toActivity(MainActivity::class.java)
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar(authProgressBar)
                    if (it.isNetworkError) {
                        toast("Check your connection!")
                    }
                    toast(it.toString())
                }
            }
        })
    }

    private fun saveUsefulUrls(response: SearchResponse) {
        for (info in response.items) {
            MyPreferences.writeString(this, KEY_AVATAR_URL, info.avatarUrl.toString())
            MyPreferences.writeString(this, KEY_HTML_URL, info.htmlUrl.toString())
        }
    }

    override fun getRepository(): BaseRepository {
        return AuthRepository(RetrofitInstance.api)
    }

    override fun getViewModel() = AuthViewModel::class.java
}