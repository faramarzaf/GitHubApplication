package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.entity.repo.RepositoryResponse.RepositoryModelItem
import app.android.githubservice.entity.starred.StarredResponse
import kotlinx.android.synthetic.main.item_list_repos.view.*


class StarredAdapter : RecyclerView.Adapter<StarredAdapter.StarredViewHolder>() {

    inner class StarredViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<StarredResponse.StarredModelItem>() {
        override fun areItemsTheSame(oldItem: StarredResponse.StarredModelItem, newItem: StarredResponse.StarredModelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StarredResponse.StarredModelItem, newItem: StarredResponse.StarredModelItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarredViewHolder {
        return StarredViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_repos, parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((StarredResponse.StarredModelItem) -> Unit)? = null

    override fun onBindViewHolder(holder: StarredViewHolder, position: Int) {
        val repoInfo = differ.currentList[position]
        holder.itemView.apply {
            //Glide.with(this).load(article.urlToImage).into(ivRepositoryResponse.RepositoryModelItemImage)
            text_repo_name.text = repoInfo.name
            text_stars.text = repoInfo.stargazersCount.toString()

            setOnClickListener {
                onItemClickListener?.let { it(repoInfo) }
            }
        }
    }

    fun setOnItemClickListener(listener: (StarredResponse.StarredModelItem) -> Unit) {
        onItemClickListener = listener
    }
}