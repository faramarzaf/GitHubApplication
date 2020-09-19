package app.android.githubservice.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.android.githubservice.KEY_IS_LOGGED_IN
import app.android.githubservice.KEY_PASSWORD
import app.android.githubservice.KEY_USERNAME
import app.android.githubservice.R
import app.android.githubservice.network.RetrofitInstance
import app.android.githubservice.repository.AuthRepository
import app.android.githubservice.repository.Resource
import app.android.githubservice.viewmodel.AuthViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.helper.StringHelper
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(this)
        initViewModel()
        handleAuthResponse()
        checkUserIsAuth()
    }

    override fun onClick(v: View?) {
        showProgressBar()
        if (editTextUsername.text.toString().trim().isNotEmpty() && editTextPassword.text.toString().trim().isNotEmpty()) {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            MyPreferences.writeString(this, KEY_USERNAME, username)
            MyPreferences.writeString(this, KEY_PASSWORD, password)
            viewModel.auth(username)
        } else if (StringHelper.stringIsEmptyOrNull(editTextUsername.text.toString().trim())
            && StringHelper.stringIsEmptyOrNull(editTextPassword.text.toString().trim())
        ) {
            hideProgressBar()
            Toast.makeText(this, "Fill fields!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUserIsAuth() {
        if (MyPreferences.readBoolean(this, KEY_IS_LOGGED_IN, false)) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        } else
            return
    }

    private fun initViewModel() {
        val newsRepository = AuthRepository(RetrofitInstance().getApi())
        val viewModelProviderFactory = ViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    private fun handleAuthResponse() {
        viewModel.loginResponse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    if (it.value.totalCount == 0) {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
                    } else {
                        hideProgressBar()
                        MyPreferences.writeBoolean(this, KEY_IS_LOGGED_IN, true)
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    if (it.isNetworkError) {
                        Toast.makeText(this, "Check your connection!", Toast.LENGTH_LONG).show()
                    }
                    Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun showProgressBar() {
        authProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        authProgressBar.visibility = View.GONE
    }

}