package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.ui.activity.LoginActivity
import app.android.githubservice.ui.adapter.ViewPagerProfileAdapter
import app.android.githubservice.util.*
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DialogCallback
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.PublicDialog
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {
    override val getFragmentLayout: Int
        get() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        MyPreferences.clearAll(requireContext())
        toActivity(activity, LoginActivity::class.java)
        requireActivity().finish()
    }
}