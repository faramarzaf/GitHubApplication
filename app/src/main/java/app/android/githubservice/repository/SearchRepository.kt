package app.android.githubservice.repository

import app.android.githubservice.entity.search.Item
import app.android.githubservice.model.db.GitHubDatabase
import app.android.githubservice.model.network.GitHubApi
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: GitHubApi, private val db: GitHubDatabase) : BaseRepository() {

    suspend fun searchUser(username: String, page: Int, per_page: Int) = safeCallApi {
        api.searchUser(username, page, per_page)
    }

    suspend fun insert(user: Item) = db.getGitHubDao().insert(user)

    fun getSavedUsers() = db.getGitHubDao().getAllUsers()

    fun userExists(user: Item) = db.getGitHubDao().userExist(user.id)

    suspend fun deleteUser(user: Item) = db.getGitHubDao().deleteUser(user)

    suspend fun deleteAll() = db.getGitHubDao().deleteAll()

}