package app.android.githubservice.model.repo

import com.google.gson.annotations.SerializedName


data class RepoModel(
    val repos: MutableList<RepoModelItem>
)