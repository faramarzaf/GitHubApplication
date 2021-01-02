package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.util.*
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_overview.*


class OverviewFragment : BaseFragment() {
    override val getFragmentLayout: Int
        get() = R.layout.fragment_overview

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillOverview()
    }

    private fun fillOverview() {
        GlideHelper.circularImage(requireContext(), MyPreferences.readString(requireContext(), KEY_AVATAR_URL, ""), avatarOverview)
        textUserNameOverview.text = MyPreferences.readString(requireContext(), KEY_USERNAME, "")
        textRepositoryOverview.text = MyPreferences.readString(requireContext(), KEY_SIZE_LIST_REPO, "")
        textFollowersOverview.text = MyPreferences.readString(requireContext(), KEY_FOLLOWERS, "")
        textFollowingOverview.text = MyPreferences.readString(requireContext(), KEY_FOLLOWINGS, "")
    }
}


