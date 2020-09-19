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
        // other threads can see immediately when a thread changes this instance
        @Volatile
        private var instance: GitHubDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            // synchronized(LOCK) ---> everything that happens inside of this block code ---> can be access by other threads at the same time
            // so with this below code we make sure that we dont set this instance while we already set it.
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, GitHubDatabase::class.java, "github_db.db").build()
    }
}