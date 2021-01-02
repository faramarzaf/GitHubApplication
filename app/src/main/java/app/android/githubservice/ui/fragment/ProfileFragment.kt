package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.ui.adapter.ViewPagerAdapter
import app.android.githubservice.util.KEY_AVATAR_URL
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {
    override val getFragmentLayout: Int
        get() = R.layout.fragment_profile


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = MyPreferences.readString(requireActivity(), KEY_AVATAR_URL, "")
        viewPager.adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, context)
        tablayout.setupWithViewPager(viewPager)

    }

}