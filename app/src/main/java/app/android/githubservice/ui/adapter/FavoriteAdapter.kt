package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.databinding.ItemListFavoriteBinding
import app.android.githubservice.entity.search.Item
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper


class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val itemBinding: ItemListFavoriteBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Item) {
            val urlRepo = item.htmlUrl
            itemView.apply {
                itemBinding.textSaveName.text = item.login.toString()
                GlideHelper.circularImage(context, item.avatarUrl.toString(), itemBinding.imageSavedUsers)
                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(urlRepo.toString())
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemBinding = ItemListFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val repoInfo = differ.currentList[position]
        holder.bind(repoInfo)
    }

    fun setOnRepoClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}