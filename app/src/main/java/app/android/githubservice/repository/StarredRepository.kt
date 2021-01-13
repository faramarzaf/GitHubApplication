package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi
import javax.inject.Inject

class StarredRepository @Inject constructor(private val api: GitHubApi) : BaseRepository() {

    suspend fun getStarredRepositories(username: String, page: Int, per_page: Int) = safeCallApi {
        api.getStarredRepositories(username,page, per_page)
    }
}