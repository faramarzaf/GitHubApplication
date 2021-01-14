package app.android.githubservice.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.android.githubservice.entity.search.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class GitHubDatabase : RoomDatabase() {

    abstract fun getGitHubDao(): GitHubDao

}