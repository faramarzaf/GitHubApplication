package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.entity.follower_following.FollowerFollowingResponse
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper
import kotlinx.android.synthetic.main.item_list_followers_following.view.*


class FollowersFollowingAdapter : RecyclerView.Adapter<FollowersFollowingAdapter.FollowersFollowingViewHolder>() {

    inner class FollowersFollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<FollowerFollowingResponse.FollowerFollowingItem>() {
        override fun areItemsTheSame(
            oldItem: FollowerFollowingResponse.FollowerFollowingItem
            , newItem: FollowerFollowingResponse.FollowerFollowingItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FollowerFollowingResponse.FollowerFollowingItem,
            newItem: FollowerFollowingResponse.FollowerFollowingItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersFollowingViewHolder {
        return FollowersFollowingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_followers_following, parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((FollowerFollowingResponse.FollowerFollowingItem) -> Unit)? = null

    override fun onBindViewHolder(holder: FollowersFollowingViewHolder, position: Int) {
        val info = differ.currentList[position]
        holder.itemView.apply {
            GlideHelper.circularImage(context, info.avatarUrl.toString(), imageFollowersFollowing)
            textFollowersFollowing.text = info.login
            setOnClickListener {
                onItemClickListener?.let { it(info) }
            }
        }
    }

    fun setOnItemClickListener(listener: (FollowerFollowingResponse.FollowerFollowingItem) -> Unit) {
        onItemClickListener = listener
    }
}