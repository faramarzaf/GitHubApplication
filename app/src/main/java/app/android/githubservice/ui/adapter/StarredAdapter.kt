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
import app.android.githubservice.entity.starred.StarredResponse
import app.android.githubservice.util.LanguageColorGenerator
import app.android.githubservice.util.URL_GITHUB
import app.android.githubservice.util.thousandPrinter


class StarredAdapter : RecyclerView.Adapter<StarredAdapter.StarredViewHolder>() {

    inner class StarredViewHolder(private val itemBinding: ItemListReposBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: StarredResponse.StarredModelItem) {
            val urlRepo = URL_GITHUB + item.fullName
            itemView.apply {
                with(itemBinding) {
                    textRepoName.text = item.fullName
                    textRepoDesc.text = item.description
                    textForks.text = item.forksCount.toString()
                    if (item.stargazersCount!! > 1000 || item.forksCount!! > 1000) {
                        val startValue = thousandPrinter(item.stargazersCount.toString())
                        val forkValue = thousandPrinter(item.forksCount.toString())
                        textStars.text = startValue
                        textForks.text = forkValue
                    } else {
                        textStars.text = "☆ " + item.stargazersCount.toString()
                        textForks.text = item.forksCount.toString()
                    }

                    val keyColor = item.language.toString()
                    val codeColor = LanguageColorGenerator.getColors(context, keyColor)
                    if (codeColor != null)
                        ViewCompat.setBackgroundTintList(viewColoredLanguage, ColorStateList.valueOf(Color.parseColor(codeColor)))

                    val language = item.language
                    textLanguage.text = language
                    if (language.isNullOrEmpty()) {
                        textLanguage.text = "-"
                        viewColoredLanguage.visibility = View.GONE
                    }
                    itemView.setOnClickListener {
                        onItemClickListener?.let {
                            it(urlRepo)
                        }
                    }
                }
            }
        }
    }


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
        val itemBinding = ItemListReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StarredViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: StarredViewHolder, position: Int) {
        val repoInfo = differ.currentList[position]
        holder.bind(repoInfo)
    }

    fun setOnRepoClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}