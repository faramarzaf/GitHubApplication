package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.entity.search.Item
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper
import kotlinx.android.synthetic.main.item_list_saved.view.*


class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.SavedViewHolder>() {

    inner class SavedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        return SavedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_saved, parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Item) -> Unit)? = null

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val repoInfo = differ.currentList[position]
        holder.itemView.apply {
            text_save_name.text = repoInfo.login.toString()
            GlideHelper.circularImage(context, repoInfo.avatarUrl.toString(), imageSavedUsers)
            setOnClickListener {
                onItemClickListener?.let { it(repoInfo) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemClickListener = listener
    }
}