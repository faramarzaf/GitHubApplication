package app.android.githubservice.model.network

import app.android.githubservice.util.BASE_URL
import com.faramarzaf.sdk.af_android_sdk.core.network.ServiceRepository

class RetrofitInstance {
    companion object {
        val api by lazy {
            ServiceRepository.ServiceBuilder.buildService(BASE_URL, GitHubApi::class.java)
        }
    }
}