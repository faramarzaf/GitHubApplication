package app.android.githubservice.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import app.android.githubservice.ui.fragment.FollowersFragment
import app.android.githubservice.ui.fragment.FollowingFragment
import app.android.githubservice.ui.fragment.ReposFragment

class ViewPagerProfileAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> return ReposFragment()
            1 -> return FollowersFragment()
            2 -> return FollowingFragment()
            else -> FollowersFragment()
        }
    }


    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Repository"
            1 -> return "Followers"
            2 -> return "Following"
        }
        return super.getPageTitle(position)
    }
}