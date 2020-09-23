package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi

class ReposRepository(private val api: GitHubApi) : BaseRepository() {

    suspend fun getRepos(username: String, page: Int, per_page: Int) = safeCallApi {
        api.getRepositories(username, page, per_page)
    }

}