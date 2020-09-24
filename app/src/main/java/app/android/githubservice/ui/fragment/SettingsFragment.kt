package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment


class SettingsFragment : BaseFragment() {
    override val getFragmentLayout: Int
        get() = R.layout.fragment_settings

    override fun newInstance(): Fragment {
        return SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}