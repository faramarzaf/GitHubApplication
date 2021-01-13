package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: GitHubApi) : BaseRepository() {

    suspend fun authUser(username: String) = safeCallApi {
        api.checkUserIsValid(username)
    }
}