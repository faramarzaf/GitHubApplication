package app.android.githubservice.di

import android.content.Context
import androidx.room.Room
import app.android.githubservice.R
import app.android.githubservice.model.db.GitHubDatabase
import app.android.githubservice.model.network.GitHubApi
import app.android.githubservice.repository.*
import app.android.githubservice.util.BASE_URL
import app.android.githubservice.util.DATABASE_NAME
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.faramarzaf.sdk.af_android_sdk.core.network.ServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGitHubDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, GitHubDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()


    @Singleton
    @Provides
    fun provideGitHubDao(database: GitHubDatabase) = database.getGitHubDao()


    @Singleton
    @Provides
    fun provideGitHubApi(): GitHubApi {
/*              return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                  .baseUrl(BASE_URL)
                  .build()
                  .create(GitHubApi::class.java)*/
        val api by lazy { ServiceRepository.ServiceBuilder.buildService(BASE_URL, GitHubApi::class.java) }
        return api
    }

    @Singleton
    @Provides
    fun provideAuthRepository(api: GitHubApi) = AuthRepository(api)

    @Singleton
    @Provides
    fun provideFollowersRepository(api: GitHubApi) = FollowersRepository(api)

    @Singleton
    @Provides
    fun provideFollowingRepository(api: GitHubApi) = FollowingRepository(api)

    @Singleton
    @Provides
    fun provideReposRepository(api: GitHubApi) = ReposRepository(api)

    @Singleton
    @Provides
    fun provideSearchRepository(api: GitHubApi, db: GitHubDatabase) = SearchRepository(api, db)

    @Singleton
    @Provides
    fun provideStarredRepository(api: GitHubApi) = StarredRepository(api)


    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_github)
                .error(R.drawable.ic_github)
        )
}