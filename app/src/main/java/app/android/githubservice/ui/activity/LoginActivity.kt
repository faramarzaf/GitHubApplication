package app.android.githubservice.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import app.android.githubservice.R
import app.android.githubservice.base.BaseActivity
import app.android.githubservice.entity.search.SearchResponse
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.AuthViewModel
import com.faramarzaf.sdk.af_android_sdk.core.helper.HashHelper
import com.faramarzaf.sdk.af_android_sdk.core.helper.StringHelper
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        transparentToolbar(this)
        btnLogin.setOnClickListener(this)
        handleAuthResponse()
        checkUserIsAuth()
    }

    override fun onClick(v: View?) {
        val username = editTextUsername.text.toString().trim()
        showProgressBar(authProgressBar)
        if (checkbox_remember_me.isChecked && !StringHelper.stringIsEmptyOrNull(editTextUsername.text.toString().trim())) {
            MyPreferences.writeString(this, KEY_USERNAME, username)
            MyPreferences.writeString(this, KEY_SESSION_ID, HashHelper.sha256(username))
            viewModel.auth(username)
        } else if (!checkbox_remember_me.isChecked && !StringHelper.stringIsEmptyOrNull(editTextUsername.text.toString().trim())) {
            MyPreferences.writeString(this, KEY_USERNAME, username)
            viewModel.auth(username)
        } else if (StringHelper.stringIsEmptyOrNull(editTextUsername.text.toString().trim())) {
            hideProgressBar(authProgressBar)
            toast("Fill fields!")
        }
    }

    private fun checkUserIsAuth() {
        if (!StringHelper.stringIsEmptyOrNull(getSessionId())) {
            toActivity(MainActivity::class.java)
            finish()
        }
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
                        saveUsefulUrls(it.value)
                        toActivity(MainActivity::class.java)
                        finish()
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

}