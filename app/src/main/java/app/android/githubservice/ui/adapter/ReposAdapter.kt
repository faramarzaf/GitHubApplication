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
import app.android.githubservice.databinding.ItemListReposBinding
import app.android.githubservice.entity.repo.RepositoryResponse.RepositoryModelItem
import app.android.githubservice.util.LanguageColorGenerator
import app.android.githubservice.util.URL_GITHUB
import app.android.githubservice.util.thousandPrinter


class ReposAdapter : RecyclerView.Adapter<ReposAdapter.ReposViewHolder>() {

    inner class ReposViewHolder(private val itemBinding: ItemListReposBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: RepositoryModelItem) {
            val urlRepo = URL_GITHUB + item.fullName
            itemView.apply {
                itemBinding.textRepoName.text = item.name
                itemBinding.textRepoDesc.text = item.description
                if (item.stargazersCount!! > 1000 || item.forksCount!! > 1000) {
                    val startValue = thousandPrinter(item.stargazersCount.toString())
                    val forkValue = thousandPrinter(item.forksCount.toString())
                    itemBinding.textStars.text = startValue
                    itemBinding.textForks.text = forkValue
                } else {
                    itemBinding.textStars.text = "☆ " + item.stargazersCount.toString()
                    itemBinding.textForks.text = item.forksCount.toString()
                }

                val keyColor = item.language.toString()
                val codeColor = LanguageColorGenerator.getColors(context, keyColor)
                if (codeColor != null)
                    ViewCompat.setBackgroundTintList(itemBinding.viewColoredLanguage, ColorStateList.valueOf(Color.parseColor(codeColor)))

                val language = item.language
                itemBinding.textLanguage.text = language
                if (language.isNullOrEmpty()) {
                    itemBinding.textLanguage.text = "-"
                    itemBinding.viewColoredLanguage.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it(urlRepo)
                    }
                }
            }
        }
    }

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
        val itemBinding = ItemListReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReposViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val repoInfo = differ.currentList[position]
        holder.bind(repoInfo)
    }

    fun setOnRepoClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

}