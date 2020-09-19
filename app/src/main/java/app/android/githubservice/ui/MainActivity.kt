package app.android.githubservice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.android.githubservice.KEY_USERNAME
import app.android.githubservice.R
import com.faramarzaf.sdk.af_android_sdk.core.util.MyPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textMain.text = MyPreferences.readString(this, KEY_USERNAME,"")
    }
}