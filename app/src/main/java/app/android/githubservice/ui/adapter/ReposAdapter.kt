package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.model.repo.RepoModelItem
import kotlinx.android.synthetic.main.item_list_repos.view.*

class ReposAdapter : RecyclerView.Adapter<ReposAdapter.ReposViewHolder>() {

    inner class ReposViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<RepoModelItem>() {
        override fun areItemsTheSame(oldItem: RepoModelItem, newItem: RepoModelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepoModelItem, newItem: RepoModelItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        return ReposViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_repos, parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((RepoModelItem) -> Unit)? = null


    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val userRepo = differ.currentList[position]
        holder.itemView.apply {

            text_repo_name.text = userRepo.name
            text_stars.text = userRepo.stargazersCount.toString()

            setOnClickListener {
                onItemClickListener?.let { it(userRepo) }
            }
        }
    }

    fun setOnItemClickListener(listener: (RepoModelItem) -> Unit) {
        onItemClickListener = listener
    }
}