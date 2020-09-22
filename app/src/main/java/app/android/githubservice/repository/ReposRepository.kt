package app.android.githubservice.repository

import app.android.githubservice.network.GitHubApi

class ReposRepository(private val api: GitHubApi) : BaseRepository() {

    suspend fun getRepos(username: String) = safeCallApi {
        api.getRepositories(username)
    }

}