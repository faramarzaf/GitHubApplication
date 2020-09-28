package app.android.githubservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.android.githubservice.repository.*


class ViewModelFactory(private val repository: BaseRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(RepositoriesViewModel::class.java) -> RepositoriesViewModel(repository as ReposRepository) as T
            modelClass.isAssignableFrom(StarredViewModel::class.java) -> StarredViewModel(repository as StarredRepository) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository as SearchRepository) as T
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> FollowersViewModel(repository as FollowersRepository) as T
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> FollowingViewModel(repository as FollowingRepository) as T
            else -> throw IllegalArgumentException("View model class not found")
        }
    }

}