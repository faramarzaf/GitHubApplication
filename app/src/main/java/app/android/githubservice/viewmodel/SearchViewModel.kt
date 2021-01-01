package app.android.githubservice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.entity.search.Item
import app.android.githubservice.entity.search.SearchResponse
import app.android.githubservice.repository.Resource
import app.android.githubservice.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    private var _searchResponse: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    val searchResponse: LiveData<Resource<SearchResponse>>
        get() = _searchResponse


    fun searchUser(username: String, page: Int, per_page: Int) =
        viewModelScope.launch {
            _searchResponse.value = searchRepository.searchUser(username, page, per_page)
        }

    fun saveUser(user: Item) = viewModelScope.launch {
        searchRepository.upsert(user)
    }

    fun deleteUser(user: Item) = viewModelScope.launch {
        searchRepository.deleteUser(user)
    }

    fun getAllUsers() = searchRepository.getSavedUsers()

    fun userExists(user: Item) = searchRepository.userExists(user)
}
