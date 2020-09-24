package app.android.githubservice.ui.activity

import android.os.Bundle
import android.view.MenuItem
import app.android.githubservice.R
import app.android.githubservice.base.BaseActivityNONVM
import app.android.githubservice.ui.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivityNONVM(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultFragment()
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.repositoriesFragment -> setFragments(R.id.container, ReposFragment().newInstance(), false)
            R.id.searchFragment -> setFragments(R.id.container, SearchFragment().newInstance(), false)
            R.id.starredFragment -> setFragments(R.id.container, StarredFragment().newInstance(), false)
            R.id.favoriteFragment -> setFragments(R.id.container, FavoriteFragment().newInstance(), false)
            R.id.settingsFragment -> setFragments(R.id.container, SettingsFragment().newInstance(), false)
        }
        return true
    }

    private fun setDefaultFragment() {
        setFragments(R.id.container, ReposFragment().newInstance(), false)
    }

}