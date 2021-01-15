package app.android.githubservice.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.entity.follower_following.FollowerFollowingResponse
import app.android.githubservice.repository.FollowingRepository
import app.android.githubservice.util.Resource
import kotlinx.coroutines.launch

class FollowingViewModel @ViewModelInject constructor(val followingRepository: FollowingRepository) : ViewModel() {

    private var _followingResponse: MutableLiveData<Resource<FollowerFollowingResponse>> = MutableLiveData()
    val followingResponse: LiveData<Resource<FollowerFollowingResponse>>
        get() = _followingResponse


    fun getFollowing(username: String, page: Int, per_page: Int) = viewModelScope.launch {
        _followingResponse.value = followingRepository.getFollowing(username,page, per_page)
    }

}
