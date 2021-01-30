package app.android.githubservice.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.githubservice.entity.event.Events
import app.android.githubservice.repository.EventsRepository
import app.android.githubservice.util.Resource
import kotlinx.coroutines.launch

class EventsViewModel @ViewModelInject constructor(private val eventsRepository: EventsRepository) : ViewModel() {

    private var _eventsResponse: MutableLiveData<Resource<Events>> = MutableLiveData()
    val eventsResponse: LiveData<Resource<Events>>
        get() = _eventsResponse

    fun getEvents() = viewModelScope.launch {
        _eventsResponse.value = eventsRepository.getEvents()
    }
}
