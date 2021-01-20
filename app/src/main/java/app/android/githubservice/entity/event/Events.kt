package app.android.githubservice.entity.event


import com.google.gson.annotations.SerializedName

class Events : ArrayList<Events.EventsItem>(){
    data class EventsItem(
        @SerializedName("actor")
        val actor: Actor,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("org")
        val org: Org,
        @SerializedName("payload")
        val payload: Payload,
        @SerializedName("public")
        val `public`: Boolean,
        @SerializedName("repo")
        val repo: Repo,
        @SerializedName("type")
        val type: String
    ) {
        data class Actor(
            @SerializedName("avatar_url")
            val avatarUrl: String,
            @SerializedName("display_login")
            val displayLogin: String,
            @SerializedName("gravatar_id")
            val gravatarId: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("login")
            val login: String,
            @SerializedName("url")
            val url: String
        )
    
        data class Org(
            @SerializedName("avatar_url")
            val avatarUrl: String,
            @SerializedName("gravatar_id")
            val gravatarId: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("login")
            val login: String,
            @SerializedName("url")
            val url: String
        )
    
        data class Payload(
            @SerializedName("action")
            val action: String
        )
    
        data class Repo(
            @SerializedName("id")
            val id: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }
}