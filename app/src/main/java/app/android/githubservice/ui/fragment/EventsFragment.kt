package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.databinding.FragmentEventsBinding
import app.android.githubservice.ui.adapter.EventsAdapter
import app.android.githubservice.util.CACHE_SIZE
import app.android.githubservice.util.Resource
import app.android.githubservice.util.TAG_LOG
import app.android.githubservice.viewmodel.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsFragment : BaseFragment() {

    private lateinit var binding: FragmentEventsBinding
    private lateinit var eventsAdapter: EventsAdapter
    private val viewModel: EventsViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_events

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventsBinding.bind(view)
        setupRecyclerView()
        observeEventsList()
        viewModel.getEvents()
        eventsAdapter.setOnItemClickListener {
            it.actor.login
        }
    }

    private fun observeEventsList() {
        showProgressBar(binding.eventsProgressBar)
        viewModel.eventsResponse.observe(viewLifecycleOwner, Observer { eventsResponse ->
            when (eventsResponse) {
                is Resource.Success -> {
                    hideProgressBar(binding.eventsProgressBar)
                    eventsAdapter.differ.submitList(eventsResponse.value)
                    binding.rvEvents.setPadding(0,0,0,0)
                }
                is Resource.Failure -> {
                    hideProgressBar(binding.eventsProgressBar)
                    if (eventsResponse.isNetworkError) {
                        toast("Check your connection!")
                    }
                    Log.d(TAG_LOG, "getFollowersAsync: $eventsResponse")
                }
            }
        })
    }


    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter()
        binding.rvEvents.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(context)
            setItemViewCacheSize(CACHE_SIZE)
        }
    }

}