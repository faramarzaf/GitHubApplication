package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.databinding.ItemListFollowersFollowingBinding
import app.android.githubservice.entity.follower_following.FollowerFollowingResponse
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper


class FollowersFollowingAdapter : RecyclerView.Adapter<FollowersFollowingAdapter.FollowersFollowingViewHolder>() {

    inner class FollowersFollowingViewHolder(private val itemBinding: ItemListFollowersFollowingBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: FollowerFollowingResponse.FollowerFollowingItem) {
            itemView.apply {
                GlideHelper.circularImage(context, item.avatarUrl.toString(), itemBinding.imageFollowersFollowing)
                itemBinding.textFollowersFollowing.text = item.login
                setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<FollowerFollowingResponse.FollowerFollowingItem>() {
        override fun areItemsTheSame(oldItem: FollowerFollowingResponse.FollowerFollowingItem, newItem: FollowerFollowingResponse.FollowerFollowingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FollowerFollowingResponse.FollowerFollowingItem, newItem: FollowerFollowingResponse.FollowerFollowingItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersFollowingViewHolder {
        val itemBinding = ItemListFollowersFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersFollowingViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((FollowerFollowingResponse.FollowerFollowingItem) -> Unit)? = null

    override fun onBindViewHolder(holder: FollowersFollowingViewHolder, position: Int) {
        val info = differ.currentList[position]
        holder.bind(info)
    }

    fun setOnItemClickListener(listener: (FollowerFollowingResponse.FollowerFollowingItem) -> Unit) {
        onItemClickListener = listener
    }
}