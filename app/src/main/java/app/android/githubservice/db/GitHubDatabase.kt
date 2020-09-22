package app.android.githubservice.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.android.githubservice.model.search.Item

@Database(entities = [Item::class], version = 1)
abstract class GitHubDatabase : RoomDatabase() {

    abstract fun getGitHubDao(): GitHubDao

    companion object {
        @Volatile
        private var instance: GitHubDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
             instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, GitHubDatabase::class.java, "github_db.db").build()
    }
}