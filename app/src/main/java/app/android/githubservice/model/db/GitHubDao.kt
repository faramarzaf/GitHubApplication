package app.android.githubservice.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import app.android.githubservice.entity.search.Item

@Dao
interface GitHubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Item): Long

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<Item>>

    @Delete
    suspend fun deleteUser(user: Item)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE id = :id)")
    fun userExist(id: Int?): Boolean

}