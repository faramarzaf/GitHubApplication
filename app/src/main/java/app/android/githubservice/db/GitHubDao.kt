package app.android.githubservice.db

import androidx.lifecycle.LiveData
import androidx.room.*
import app.android.githubservice.model.search.Item

@Dao
interface GitHubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Item): Long

    @Query("SELECT * FROM users")
    fun getUser(): LiveData<List<Item>>

    @Delete
    suspend fun deleteUser(user: Item)
}