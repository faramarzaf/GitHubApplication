package app.android.githubservice.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import app.android.githubservice.ui.fragment.FollowersFragment
import app.android.githubservice.ui.fragment.FollowingFragment
import app.android.githubservice.ui.fragment.OverviewFragment

class ViewPagerOverviewAdapter(fm: FragmentManager, private val context: Context?) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> return FollowersFragment()
            1 -> return FollowingFragment()
            else -> FollowersFragment()
        }
    }


    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Following"
            1 -> return "Followers"
        }
        return super.getPageTitle(position)
    }
}