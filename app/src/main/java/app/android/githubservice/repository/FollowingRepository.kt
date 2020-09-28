package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi

class FollowingRepository(private val api: GitHubApi) : BaseRepository() {

    suspend fun getFollowing(username: String, page: Int, per_page: Int) = safeCallApi {
        api.getFollowing(username,page, per_page)
    }
}