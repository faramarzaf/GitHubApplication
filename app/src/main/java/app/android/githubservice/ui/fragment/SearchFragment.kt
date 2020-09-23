package app.android.githubservice.ui.fragment

import androidx.fragment.app.Fragment
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment

class SearchFragment : BaseFragment() {


    override fun newInstance(): Fragment {
        return SearchFragment()
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_search
    }


}