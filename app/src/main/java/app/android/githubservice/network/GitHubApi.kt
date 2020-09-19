package app.android.githubservice.network

import app.android.githubservice.model.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("/search/users")
    suspend fun checkUserIsValid(@Query("q") username: String): SearchResponse
}