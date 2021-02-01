package app.android.githubservice.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import com.faramarzaf.sdk.af_android_sdk.core.helper.NetworkHelper.Companion.checkNetwork
import com.faramarzaf.sdk.af_android_sdk.core.helper.RecyclerviewHelper
import com.faramarzaf.sdk.af_android_sdk.core.helper.ScreenHelper
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.ProgressDialogCustom
import java.util.*

abstract class BaseFragment : Fragment() {
    protected var mProgressDialog: ProgressDialogCustom? = null

    protected abstract val getFragmentLayout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getFragmentLayout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { transparentToolbar(it) }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }


    override fun onDetach() {
        super.onDetach()
    }

    protected fun showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialogCustom(
                    activity,
                    R.layout.layout_dialog_progress,
                    R.drawable.ic_launcher_background, false
                )
            }
            mProgressDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun hideProgressDialog() {
        try {
            if (activity != null && mProgressDialog != null) {
                mProgressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgressBar(pg: ProgressBar) {
        pg.visibility = View.VISIBLE
    }

    fun hideProgressBar(pg: ProgressBar) {
        pg.visibility = View.GONE
    }


    fun toast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    fun setRecyclerviewDivider(context: Context, recyclerView: RecyclerView, resId: Int) {
        RecyclerviewHelper.setRecyclerviewDivider(context, recyclerView, resId)
    }


    fun setFragments(layout: Int, fragment: Fragment, addToBackStack: Boolean) {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(layout, fragment)
        if (addToBackStack) transaction.addToBackStack(fragment.javaClass.simpleName)
        transaction.commitAllowingStateLoss()
    }

    fun toActivityWithSharedElement(activity: Activity?, destination: Class<*>?, view: View?) {
        val intent = Intent(activity, destination)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(), requireView(),
            Objects.requireNonNull(ViewCompat.getTransitionName(requireView())).toString()
        )
        startActivity(intent, options.toBundle())
    }


    open fun toActivity(activity: Activity?, classOf: Class<*>) {
        startActivity(Intent(activity, classOf))
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


    fun transparentToolbar(activity: Activity) {
        ScreenHelper.hideToolbar(activity)
    }

    fun isNetworkConnected(): Boolean {
        return checkNetwork(requireContext())
    }
}