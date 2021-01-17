package app.android.githubservice.ui

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import app.android.githubservice.R
import app.android.githubservice.base.BaseBottomSheet
import app.android.githubservice.interfaces.GlobalBottomSheetCallBack
import app.android.githubservice.util.DELAY_GUARD_CLICK
import app.android.githubservice.util.KEY_THEME
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DoGuardTask
import com.faramarzaf.sdk.af_android_sdk.core.util.ClickGuard
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

open class BottomSheetTheme : BaseBottomSheet(), DoGuardTask, CompoundButton.OnCheckedChangeListener {

    override val fragmentLayout: Int
        get() = R.layout.fragment_bottom_sheet

    private var globalBottomSheetCallBack: GlobalBottomSheetCallBack? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadTheme()
        ClickGuard.guardView(imgLogout, DELAY_GUARD_CLICK, this)
        ClickGuard.guardView(switchTheme, DELAY_GUARD_CLICK, this)
        switchTheme.setOnCheckedChangeListener(this)
    }


    fun loadTheme() {
        if (MyPreferences.readBoolean(requireContext(), KEY_THEME, false)) {
            setDarkTheme()
        } else {
            setLightTheme()
        }
    }

    override fun onGuard(view: View) {
        when (view.id) {
            R.id.imgLogout -> {
                globalBottomSheetCallBack!!.onLogoutClick()
            }
        }
    }

    fun setOnBottomSheetClickListener(listener: (GlobalBottomSheetCallBack)) {
        globalBottomSheetCallBack = listener
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (isChecked) {
            true -> {
                setDarkTheme()
                MyPreferences.writeBoolean(requireContext(), KEY_THEME, true)
            }
            false -> {
                setLightTheme()
                MyPreferences.writeBoolean(requireContext(), KEY_THEME, false)
            }
        }
    }

    private fun setDarkTheme() {
        (activity as AppCompatActivity?)!!.delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun setLightTheme() {
        (activity as AppCompatActivity?)!!.delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    }
}