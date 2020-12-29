package app.android.githubservice.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.entity.LanguageColor
import app.android.githubservice.entity.repo.RepositoryResponse.RepositoryModelItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_list_repos.view.*


class ReposAdapter : RecyclerView.Adapter<ReposAdapter.ReposViewHolder>() {

    inner class ReposViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<RepositoryModelItem>() {
        override fun areItemsTheSame(oldItem: RepositoryModelItem, newItem: RepositoryModelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepositoryModelItem, newItem: RepositoryModelItem): Boolean {
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

    private var onItemClickListener: ((RepositoryModelItem) -> Unit)? = null

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val repoInfo = differ.currentList[position]
        holder.itemView.apply {
            text_repo_name.text = repoInfo.name
            text_stars.text = repoInfo.stargazersCount.toString()
            val language = repoInfo.language
            val objarraystring: String = context.resources.openRawResource(R.raw.colors).bufferedReader().use { it.readText() }
            val objectarray = Gson().fromJson(objarraystring, LanguageColor::class.java)
            text_language.text = language
            if (language.isNullOrEmpty())
                text_language.text = "-"
            setOnClickListener {
                onItemClickListener?.let { it(repoInfo) }
            }
        }
    }

    fun setOnItemClickListener(listener: (RepositoryModelItem) -> Unit) {
        onItemClickListener = listener
    }


}