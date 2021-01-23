package app.android.githubservice.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.entity.repo.RepositoryResponse
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.util.Resource
import kotlinx.coroutines.launch

class RepositoriesViewModel @ViewModelInject constructor(private val reposRepository: ReposRepository) : ViewModel() {

    private var _reposResponse: MutableLiveData<Resource<RepositoryResponse>> = MutableLiveData()
    val reposResponse: LiveData<Resource<RepositoryResponse>>
        get() = _reposResponse


    fun getRepos(username: String, page: Int, per_page: Int) = viewModelScope.launch {
        _reposResponse.value = reposRepository.getRepos(username, page, per_page)
    }

}