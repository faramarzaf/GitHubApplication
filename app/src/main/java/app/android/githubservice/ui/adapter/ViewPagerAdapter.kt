package app.android.githubservice.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import app.android.githubservice.ui.fragment.FollowersFragment
import app.android.githubservice.ui.fragment.FollowingFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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
            0 -> return "Followers"
            1 -> return "Following"
        }
        return super.getPageTitle(position)
    }

}