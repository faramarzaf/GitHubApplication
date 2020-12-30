package app.android.githubservice.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.entity.repo.RepositoryResponse.RepositoryModelItem
import app.android.githubservice.util.LanguageColorGenerator
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
            if (repoInfo.stargazersCount!! > 1000) {
                val startValue = "☆ " + repoInfo.stargazersCount.toString().substring(0, 2) + "K"
                text_stars.text = startValue
            } else
                text_stars.text = "☆ " + repoInfo.stargazersCount.toString()

            val keyColor = repoInfo.language.toString()
            val codeColor = LanguageColorGenerator.getColors(context, keyColor)
            if (codeColor != null)
                ViewCompat.setBackgroundTintList(view_colored_language, ColorStateList.valueOf(Color.parseColor(codeColor)))

            val language = repoInfo.language
            text_language.text = language
            if (language.isNullOrEmpty()) {
                text_language.text = "-"
                view_colored_language.visibility = View.GONE
            }

            setOnClickListener {
                onItemClickListener?.let { it(repoInfo) }
            }
        }
    }

    fun setOnItemClickListener(listener: (RepositoryModelItem) -> Unit) {
        onItemClickListener = listener
    }

}