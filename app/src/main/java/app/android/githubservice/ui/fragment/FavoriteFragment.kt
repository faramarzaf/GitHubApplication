package app.android.githubservice.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment

class FavoriteFragment  : BaseFragment() {
    override val getFragmentLayout: Int
        get() = R.layout.fragment_favorite

    override fun newInstance(): Fragment {
       return FavoriteFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}