package app.android.githubservice.network

import com.faramarzaf.sdk.af_android_sdk.core.network.ServiceRepository

class RetrofitInstance {

    private val gitHubService = ServiceRepository.ServiceBuilder
        .buildService("", GitHubApi::class.java)

    fun getApi() {
    }

}