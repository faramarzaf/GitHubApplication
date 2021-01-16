package app.android.githubservice.ui

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import app.android.githubservice.R
import app.android.githubservice.base.BaseBottomSheet
import app.android.githubservice.interfaces.GlobalBottomSheetCallBack
import app.android.githubservice.util.DELAY_GUARD_CLICK
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DoGuardTask
import com.faramarzaf.sdk.af_android_sdk.core.util.ClickGuard
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

open class BottomSheetTheme : BaseBottomSheet(), DoGuardTask, CompoundButton.OnCheckedChangeListener {

    override val fragmentLayout: Int
        get() = R.layout.fragment_bottom_sheet

    private var globalBottomSheetCallBack: GlobalBottomSheetCallBack? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ClickGuard.guardView(imgLogout, DELAY_GUARD_CLICK, this)
        ClickGuard.guardView(switchTheme, DELAY_GUARD_CLICK, this)
        switchTheme.setOnCheckedChangeListener(this)
    }


    override fun onGuard(view: View) {
        when (view.id) {
            R.id.imgLogout -> globalBottomSheetCallBack!!.onLogoutClick()
        }
    }

    fun setOnBottomSheetClickListener(listener: (GlobalBottomSheetCallBack)) {
        globalBottomSheetCallBack = listener
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (isChecked) {
            true -> globalBottomSheetCallBack!!.onLightThemeClick()
            false -> globalBottomSheetCallBack!!.onDarkThemeClick()
        }
    }

}