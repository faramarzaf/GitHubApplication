package app.android.githubservice.repository

import app.android.githubservice.model.network.GitHubApi
import javax.inject.Inject

class EventsRepository @Inject constructor(private val api: GitHubApi) : BaseRepository() {

    suspend fun getEvents() = safeCallApi {
        api.getEvents()
    }
}