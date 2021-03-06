package app.android.githubservice.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.databinding.FragmentProfileBinding
import app.android.githubservice.interfaces.GlobalBottomSheetCallBack
import app.android.githubservice.ui.BottomSheetProfile
import app.android.githubservice.ui.activity.LoginActivity
import app.android.githubservice.ui.adapter.ViewPagerProfileAdapter
import app.android.githubservice.util.*
import app.android.githubservice.viewmodel.SearchViewModel
import com.faramarzaf.sdk.af_android_sdk.core.helper.GlideHelper
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DialogCallback
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DoGuardTask
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.PublicDialog
import com.faramarzaf.sdk.af_android_sdk.core.util.ClickGuard
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*


@AndroidEntryPoint
class ProfileFragment : BaseFragment(), DoGuardTask {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private val bottomSheetTheme = BottomSheetProfile()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        with(binding) {
            viewPagerOverview.adapter = ViewPagerProfileAdapter(requireActivity().supportFragmentManager)
            tabLayoutOverview.setupWithViewPager(viewPagerOverview)
        }
        fillProfileInfo()
        bottomSheetOperations()
        ClickGuard.guardView(imgSetting, 800, this)
    }


    private fun bottomSheetOperations() {
        bottomSheetTheme.setOnBottomSheetClickListener(object : GlobalBottomSheetCallBack {
            override fun onLogoutClick() {
                openLogoutDialog()
            }
        })
    }

    private fun fillProfileInfo() {
        with(binding) {
            GlideHelper.circularImage(requireContext(), MyPreferences.readString(requireContext(), KEY_AVATAR_URL, ""), avatarProfile)
            textUserNameProfile.text = MyPreferences.readString(requireContext(), KEY_USERNAME, "")
            textRepositoryProfile.text = MyPreferences.readString(requireContext(), KEY_SIZE_LIST_REPO, "")
            textFollowersProfile.text = MyPreferences.readString(requireContext(), KEY_NUMBER_FOLLOWERS, "")
            textFollowingProfile.text = MyPreferences.readString(requireContext(), KEY_NUMBER_FOLLOWING, "")
        }
    }

    override fun onGuard(view: View) {
        bottomSheetTheme.show(requireActivity().supportFragmentManager, TAG_BOTTOM_SHEET)
    }

    private fun openLogoutDialog() {
        PublicDialog.yesNoDialog(requireContext(), getString(R.string.logout_title), getString(R.string.msg_dialog_logout)
            , getString(R.string.yes), getString(R.string.no), R.drawable.ic_logout, object : DialogCallback {
                override fun onNegativeButtonClicked() {
                    return
                }

                override fun onPositiveButtonClicked() {
                    logout()
                }
            })
    }

    private fun logout() {
        viewModel.deleteAll()
        MyPreferences.clearAll(requireContext())
        toActivity(activity, LoginActivity::class.java)
        requireActivity().finish()
    }
}