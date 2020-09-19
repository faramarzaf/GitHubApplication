package app.android.githubservice.network

import app.android.githubservice.BASE_URL
import com.faramarzaf.sdk.af_android_sdk.core.network.ServiceRepository

class RetrofitInstance {

    fun getApi(): GitHubApi {
        return ServiceRepository.ServiceBuilder.buildService(BASE_URL, GitHubApi::class.java)
    }

}