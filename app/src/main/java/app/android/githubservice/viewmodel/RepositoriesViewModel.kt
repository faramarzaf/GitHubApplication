package app.android.githubservice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.model.repo.RepoModel
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.repository.Resource
import kotlinx.coroutines.launch

class RepositoriesViewModel(val reposRepository: ReposRepository) : ViewModel() {

    private var _reposResponse: MutableLiveData<Resource<RepoModel>> = MutableLiveData()
    val reposResponse: LiveData<Resource<RepoModel>>
        get() = _reposResponse


    fun getRepos(username: String) = viewModelScope.launch {
        _reposResponse.value = reposRepository.getRepos(username)
    }

}