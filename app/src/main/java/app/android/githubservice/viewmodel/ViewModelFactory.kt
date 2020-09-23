package app.android.githubservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.android.githubservice.repository.AuthRepository
import app.android.githubservice.repository.BaseRepository
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.StarredRepository


class ViewModelFactory(private val repository: BaseRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(RepositoriesViewModel::class.java) -> RepositoriesViewModel(repository as ReposRepository) as T
            modelClass.isAssignableFrom(StarredViewModel::class.java) -> StarredViewModel(repository as StarredRepository) as T
            else -> throw IllegalArgumentException("View model class not found")
        }
    }

}