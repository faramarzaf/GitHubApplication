package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.databinding.ItemListSearchedUsersBinding
import app.android.githubservice.entity.search.Item
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper
import kotlinx.android.synthetic.main.item_list_searched_users.view.*


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val itemBinding: ItemListSearchedUsersBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(items: Item) {
            val imageViewFav = itemView.imageFav
            itemBinding.imageFav.setOnClickListener {
                onSaveUserClickListener?.let { item ->
                    item(items, imageViewFav)
                }
            }
            getItemInstance?.let {
                it(items)
            }

            itemView.apply {
                GlideHelper.circularImage(context, items.avatarUrl.toString(), itemBinding.avatarUser)
                itemBinding.textUser.text = items.login
                setOnClickListener {
                    onItemClickListener?.let { it(items) }
                }
                getViewFromAdapter?.let { views ->
                    views(imageFav)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemBinding = ItemListSearchedUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Item) -> Unit)? = null
    private var onSaveUserClickListener: ((Item, ImageView) -> Unit)? = null
    private var getItemInstance: ((Item) -> Unit)? = null
    private var getViewFromAdapter: ((ImageView) -> Unit)? = null

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchInfo = differ.currentList[position]
        holder.bind(searchInfo)
    }

    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnSaveUserClickListener(listener: (Item, ImageView) -> Unit) {
        onSaveUserClickListener = listener
    }

    fun getItemInstance(listener: (Item) -> Unit) {
        getItemInstance = listener
    }

    fun getViewFromAdapter(listener: (ImageView) -> Unit) {
        getViewFromAdapter = listener
    }

}