package app.android.githubservice.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.faramarzaf.sdk.af_android_sdk.core.helper.NetworkHelper;
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.ProgressDialogCustom;
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences;

import java.util.Objects;

import app.android.githubservice.R;

import static app.android.githubservice.util.ConstantsKt.KEY_SESSION_ID;

public abstract class BaseFragment extends Fragment {

    private String TAG = BaseFragment.class.getSimpleName();

    protected ProgressDialogCustom mProgressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    protected void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialogCustom(getActivity(),
                        R.layout.layout_dialog_progress,
                        R.drawable.ic_launcher_background, false);
            }
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideProgressDialog() {
        try {
            if (getActivity() != null && mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }


    protected abstract int getFragmentLayout();


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public String getSessionId() {
        return MyPreferences.Prefs.readString(requireContext(), KEY_SESSION_ID, "");
    }

    public void setFragments(int layout, Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(layout, fragment);
        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public void toActivityWithSharedElement(Activity activity, Class<?> destination, View view) {
        Intent intent = new Intent(activity, destination);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, Objects.requireNonNull(ViewCompat.getTransitionName(view)));
        startActivity(intent, options.toBundle());
    }

/*    public void showAlerter(String title, String message) {
        Alerter.create(getActivity())
                .setTitle(title)
                .setText(message)
                .setContentGravity(Gravity.END)
                .setTitleTypeface(Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), getActivity().getResources().getString(R.string.font_address)))
                .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), getActivity().getResources().getString(R.string.font_address)))
                .setBackgroundColorRes(R.color.colorAccent)
                .setIcon(R.mipmap.ic_launcher)
                .setIconColorFilter(0) // Optional - Removes white tint
                .show();
    }*/

    public boolean isNetworkConnected(Context context) {
        return NetworkHelper.Companion.checkNetwork(context);
    }
}
