package app.android.githubservice.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.entity.follower_following.FollowerFollowingResponse
import app.android.githubservice.entity.search.SearchResponse
import app.android.githubservice.repository.FollowersRepository
import app.android.githubservice.util.Resource
import kotlinx.coroutines.launch

class FollowersViewModel @ViewModelInject constructor(private val followersRepository: FollowersRepository) : ViewModel() {

    private var _followersResponse: MutableLiveData<Resource<FollowerFollowingResponse>> = MutableLiveData()
    val followersResponse: LiveData<Resource<FollowerFollowingResponse>>
        get() = _followersResponse


    fun getFollowers(username: String, page: Int, per_page: Int) = viewModelScope.launch {
        _followersResponse.value = followersRepository.getFollowers(username,page, per_page)
    }

}
