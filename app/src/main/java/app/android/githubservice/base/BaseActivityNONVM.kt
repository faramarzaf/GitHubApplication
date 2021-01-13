package app.android.githubservice.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.android.githubservice.R
import app.android.githubservice.interfaces.AppDialogCallback
import app.android.githubservice.util.KEY_SESSION_ID
import com.faramarzaf.sdk.af_android_sdk.core.helper.NetworkHelper
import com.faramarzaf.sdk.af_android_sdk.core.helper.ScreenHelper
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DialogCallback
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.ProgressDialogCustom
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.SimpleDialog
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


abstract class BaseActivityNONVM : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun transparentToolbar(activity: Activity) {
        ScreenHelper.hideToolbar(activity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
