package app.android.githubservice.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.android.githubservice.base.BaseActivity
import app.android.githubservice.databinding.ActivityLoginBinding
import app.android.githubservice.entity.search.SearchResponse
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.AuthViewModel
import com.faramarzaf.sdk.af_android_sdk.core.helper.HashHelper
import com.faramarzaf.sdk.af_android_sdk.core.helper.StringHelper
import com.faramarzaf.sdk.af_android_sdk.core.util.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUserIsAuth()
        transparentToolbar(this)
        handleAuthResponse()
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val username = binding.editTextUsername.text.toString().trim()
        showProgressBar(binding.authProgressBar)
        if (binding.checkboxRememberMe.isChecked && !StringHelper.stringIsEmptyOrNull(binding.editTextUsername.text.toString().trim())) {
            writeUsername(username)
            writeSessionId(username)
            viewModel.auth(username)
        } else if (!binding.checkboxRememberMe.isChecked && !StringHelper.stringIsEmptyOrNull(binding.editTextUsername.text.toString().trim())) {
            writeUsername(username)
            viewModel.auth(username)
        } else if (StringHelper.stringIsEmptyOrNull(binding.editTextUsername.text.toString().trim())) {
            hideProgressBar(binding.authProgressBar)
            toast("Fill fields!")
        }
    }

    private fun writeUsername(username: String) {
        lifecycleScope.launch {
            MyDataStore(this@LoginActivity).writeString(KEY_USERNAME, username)
        }
    }

    private fun writeSessionId(username: String) {
        lifecycleScope.launch {
            MyDataStore(this@LoginActivity).writeString(KEY_SESSION_ID, HashHelper.sha256(username))
        }
    }

    private fun checkUserIsAuth() {
        lifecycleScope.launch {
            if (!StringHelper.stringIsEmptyOrNull(getSessionId())) {
                toActivity(MainActivity::class.java)
                finish()
            }
        }
    }

    private fun handleAuthResponse() {
        viewModel.loginResponse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar(binding.authProgressBar)
                    if (it.value.totalCount == 0) {
                        toast("User not found!")
                    } else {
                        hideProgressBar(binding.authProgressBar)
                        saveUsefulUrls(it.value)
                        toActivity(MainActivity::class.java)
                        finish()
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar(binding.authProgressBar)
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
            lifecycleScope.launch {
                MyDataStore(this@LoginActivity).writeString(KEY_AVATAR_URL, info.avatarUrl.toString())
                MyDataStore(this@LoginActivity).writeString(KEY_HTML_URL, info.htmlUrl.toString())
            }
        }
    }

}