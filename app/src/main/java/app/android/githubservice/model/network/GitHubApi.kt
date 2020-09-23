package app.android.githubservice.model.network

import app.android.githubservice.entity.repo.RepositoryResponse
import app.android.githubservice.entity.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("/search/users")
    suspend fun checkUserIsValid(@Query("q") username: String): SearchResponse


    @GET("/search/users")
    suspend fun searchUser(@Query("q") username: String): SearchResponse


    @GET("/users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") username: String
        , @Query("page") page: Int
        , @Query("per_page") per_page: Int
    ): RepositoryResponse

}