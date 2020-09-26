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
import kotlinx.android.synthetic.main.item_list_searched_users.view.*


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_searched_users, parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Item) -> Unit)? = null
    private var onSaveUserClickListener: ((Item) -> Unit)? = null

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchInfo = differ.currentList[position]
        holder.itemView.apply {
            GlideHelper.circularImage(context, searchInfo.avatarUrl.toString(), avatarUser)
            textUser.text = searchInfo.login
            setOnClickListener {
                onItemClickListener?.let { it(searchInfo) }
            }

            imageFav.setOnClickListener {
                onSaveUserClickListener?.let {
                    it(searchInfo)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnSaveUserClickListener(listener: (Item) -> Unit) {
        onSaveUserClickListener = listener
    }
}