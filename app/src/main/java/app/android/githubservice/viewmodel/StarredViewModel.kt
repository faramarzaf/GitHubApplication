package app.android.githubservice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.entity.starred.StarredResponse
import app.android.githubservice.repository.Resource
import app.android.githubservice.repository.StarredRepository
import kotlinx.coroutines.launch

class StarredViewModel(val starredRepository: StarredRepository) : ViewModel() {

    private var _starredResponse: MutableLiveData<Resource<StarredResponse>> = MutableLiveData()
    val starredResponse: LiveData<Resource<StarredResponse>>
        get() = _starredResponse


    fun getStarredRepositories(username: String, page: Int, per_page: Int) = viewModelScope.launch {
        _starredResponse.value = starredRepository.getStarredRepositories(username, page, per_page)
    }

}