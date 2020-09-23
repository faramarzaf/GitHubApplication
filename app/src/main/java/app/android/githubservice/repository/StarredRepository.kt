package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi

class StarredRepository(private val api: GitHubApi) : BaseRepository() {

    suspend fun getStarredRepositories(username: String, page: Int, per_page: Int) = safeCallApi {
        api.getStarredRepositories(username,page, per_page)
    }
}