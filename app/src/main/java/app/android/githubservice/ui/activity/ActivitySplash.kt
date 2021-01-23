package app.android.githubservice.ui.activity

import android.os.Bundle
import android.os.Handler
import app.android.githubservice.R
import app.android.githubservice.base.BaseActivity

class ActivitySplash : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        transparentToolbar(this)
        loadSplash()
    }

    private fun loadSplash() {
        Handler().postDelayed({
            toActivity(LoginActivity::class.java)
            finish()
        }, 1600)
    }


}