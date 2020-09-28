package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi

class FollowersRepository(private val api: GitHubApi) : BaseRepository() {

    suspend fun getFollowers(username: String, page: Int, per_page: Int) = safeCallApi {
        api.getFollowers(username,page, per_page)
    }
}