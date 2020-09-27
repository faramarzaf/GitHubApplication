package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.ui.adapter.ViewPagerAdapter
import app.android.githubservice.util.AVATAR_URL
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : BaseFragment() {
    override val getFragmentLayout: Int
        get() = R.layout.fragment_settings


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = MyPreferences.readString(requireActivity(), AVATAR_URL, "")
        /* GlideHelper.circularImage(requireActivity(), imageUrl, imageProfile)
         textUserName.text = MyPreferences.readString(requireActivity(), KEY_USERNAME, "")*/

        viewPager.adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        tablayout.setupWithViewPager(viewPager)

    }


}