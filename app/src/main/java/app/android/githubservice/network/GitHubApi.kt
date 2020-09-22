package app.android.githubservice.network

import app.android.githubservice.model.repo.RepoModel
import app.android.githubservice.model.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("/search/users")
    suspend fun checkUserIsValid(@Query("q") username: String): SearchResponse


    @GET("/search/users")
    suspend fun searchUser(@Query("q") username: String): SearchResponse


    @GET("/users/{user}/repos")
    suspend fun getRepositories(@Path("user") username: String): RepoModel


}