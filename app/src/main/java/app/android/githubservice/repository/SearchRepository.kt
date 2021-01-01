package app.android.githubservice.repository

import app.android.githubservice.entity.search.Item
import app.android.githubservice.model.db.GitHubDatabase
import app.android.githubservice.model.network.GitHubApi

class SearchRepository(private val api: GitHubApi, private val db: GitHubDatabase) : BaseRepository() {

    suspend fun searchUser(username: String, page: Int, per_page: Int) = safeCallApi {
        api.searchUser(username, page, per_page)
    }

    suspend fun upsert(user: Item) = db.getGitHubDao().upsert(user)

    fun getSavedUsers() = db.getGitHubDao().getAllUsers()

    fun userExists(user: Item) = db.getGitHubDao().isUserExist(user.id)

    suspend fun deleteUser(user: Item) = db.getGitHubDao().deleteUser(user)

}