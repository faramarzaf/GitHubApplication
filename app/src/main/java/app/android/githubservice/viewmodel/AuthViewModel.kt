package app.android.githubservice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.model.search.SearchResponse
import app.android.githubservice.repository.AuthRepository
import app.android.githubservice.repository.Resource
import kotlinx.coroutines.launch

class AuthViewModel(val authRepository: AuthRepository) : ViewModel() {

    private var _loginResponse: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<SearchResponse>>
        get() = _loginResponse


    fun auth(username: String) = viewModelScope.launch {
        _loginResponse.value = authRepository.authUser(username)
    }

}
