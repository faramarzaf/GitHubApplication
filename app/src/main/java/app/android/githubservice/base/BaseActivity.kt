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
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.interfaces.AppDialogCallback
import app.android.githubservice.util.KEY_SESSION_ID
import com.faramarzaf.sdk.af_android_sdk.core.helper.NetworkHelper
import com.faramarzaf.sdk.af_android_sdk.core.helper.ScreenHelper
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DialogCallback
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.ProgressDialogCustom
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.SimpleDialog
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import java.util.*


abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialogCustom? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun toActivity(classOf: Class<*>) {
        startActivity(Intent(applicationContext, classOf))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun toActivityWithSharedElement(activity: Activity, destination: Class<*>, view: View) {
        val intent = Intent(activity, destination)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, view, Objects.requireNonNull<String>(ViewCompat.getTransitionName(view))
        )
        startActivity(intent, options.toBundle())
    }

    fun setFragments(layout: Int, fragment: Fragment, addToBackStack: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(layout, fragment)
        if (addToBackStack)
            transaction.addToBackStack(fragment.javaClass.simpleName)
        transaction.commitAllowingStateLoss()
    }

    fun getSessionId(context: Context): String {
        return MyPreferences.readString(context, KEY_SESSION_ID, "")
    }

    fun getSessionId(): String {
        return MyPreferences.readString(this, KEY_SESSION_ID, "")
    }


    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    open fun showProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialogCustom(
                    this,
                    R.layout.layout_dialog_progress,
                    R.drawable.ic_launcher_background,
                    false
                )
            }
            progressDialog!!.show()

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    open fun hideProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog!!.dismiss()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun showSimpleDialog(content: String, negativeText: String, positiveText: String, callback: AppDialogCallback) {
        SimpleDialog(this)
            .cancelable(true)
            .setText(content)
            .setPositiveButton(positiveText)
            .setNegativeButton(negativeText)
            .showDialog()
            .setTypeface(getString(R.string.font_address))
            .setTitleColor(Color.rgb(0, 0, 0))
            .setTextSize(16f, 13f)
            .setPositiveBackground(R.drawable.shape_round_corner_button, Color.WHITE)
            .setNegativeBackground(R.drawable.shape_round_corner_button, Color.WHITE)
            .setImageDialogBackground(R.drawable.ic_launcher_background)
            .setDialogBackground(R.drawable.shape_dialog)
            .setCallBack(object : DialogCallback {
                override fun onPositiveButtonClicked() {
                    callback.onConfirm()
                }

                override fun onNegativeButtonClicked() {
                    callback.onCancel()
                }
            })
    }

    fun transparentToolbar(activity: Activity) {
        ScreenHelper.hideToolbar(activity)
    }


    fun isNetworkConnected(context: Context): Boolean {
        return NetworkHelper.checkNetwork(context)
    }


    fun showProgressBar(pg: ProgressBar) {
        pg.visibility = View.VISIBLE
    }

    fun hideProgressBar(pg: ProgressBar) {
        pg.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
