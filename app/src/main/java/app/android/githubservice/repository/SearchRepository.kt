package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi

class SearchRepository(private val api: GitHubApi) : BaseRepository() {

    suspend fun searchUser(username: String, page: Int, per_page: Int) = safeCallApi {
        api.searchUser(username,page, per_page)
    }
}