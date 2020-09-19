package app.android.githubservice.repository

import app.android.githubservice.network.GitHubApi

class AuthRepository(private val api: GitHubApi) : BaseRepository() {

    suspend fun authUser(username: String) = safeCallApi {
        api.checkUserIsValid(username)
    }


}