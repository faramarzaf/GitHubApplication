package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.di.AppModule
import app.android.githubservice.model.db.GitHubDatabase
import app.android.githubservice.model.network.GitHubApi

import app.android.githubservice.repository.SearchRepository
import app.android.githubservice.ui.activity.LoginActivity
import app.android.githubservice.ui.adapter.ViewPagerProfileAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.SearchViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DialogCallback
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.PublicDialog
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var api: GitHubApi
    @Inject
    lateinit var database: GitHubDatabase

    override val getFragmentLayout: Int
        get() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewPagerOverview.adapter = ViewPagerProfileAdapter(requireActivity().supportFragmentManager)
        tabLayoutOverview.setupWithViewPager(viewPagerOverview)
        fillOverview()
        imgLogout.setOnClickListener {
            openLogoutDialog()
        }
    }

    private fun fillOverview() {
        GlideHelper.circularImage(requireContext(), MyPreferences.readString(requireContext(), KEY_AVATAR_URL, ""), avatarOverview)
        textUserNameOverview.text = MyPreferences.readString(requireContext(), KEY_USERNAME, "")
        textRepositoryOverview.text = MyPreferences.readString(requireContext(), KEY_SIZE_LIST_REPO, "")
        textFollowersOverview.text = MyPreferences.readString(requireContext(), KEY_NUMBER_FOLLOWERS, "")
        textFollowingOverview.text = MyPreferences.readString(requireContext(), KEY_NUMBER_FOLLOWING, "")
    }

    private fun openLogoutDialog() {
        PublicDialog.yesNoDialog(requireContext(), getString(R.string.logout_title), getString(R.string.msg_dialog_logout)
            , getString(R.string.yes), getString(R.string.no), R.drawable.ic_logout, object : DialogCallback {
                override fun onNegativeButtonClicked() {
                    return
                }

                override fun onPositiveButtonClicked() {
                    logout()
                }
            })
    }

    private fun logout() {
        viewModel.deleteAll()
        MyPreferences.clearAll(requireContext())
        toActivity(activity, LoginActivity::class.java)
        requireActivity().finish()
    }

    private fun initViewModel() {
        val factory = ViewModelFactory(SearchRepository(api,database))
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
    }
}