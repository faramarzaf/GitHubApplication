package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.databinding.ItemListEventsBinding
import app.android.githubservice.entity.event.Events
import app.android.githubservice.util.EventPageDataProvider
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper


class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    inner class EventsViewHolder(private val itemBinding: ItemListEventsBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Events.EventsItem) {
            itemView.apply {
                val event = EventPageDataProvider.getEvent(item)
                with(itemBinding) {
                    textEventType.text = "Action: $event"
                    textUsernameEvent.text = item.actor.login
                    textRepoNameEvent.text = item.repo.name
                    GlideHelper.circularImage(context, item.actor.avatarUrl, avatarEvent)
                }
                setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Events.EventsItem>() {

        override fun areItemsTheSame(oldItem: Events.EventsItem, newItem: Events.EventsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Events.EventsItem, newItem: Events.EventsItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val itemBinding = ItemListEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Events.EventsItem) -> Unit)? = null

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val repoInfo = differ.currentList[position]
        holder.bind(repoInfo)
    }

    fun setOnItemClickListener(listener: (Events.EventsItem) -> Unit) {
        onItemClickListener = listener
    }
}