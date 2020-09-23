package app.android.githubservice.ui.activity

import android.os.Bundle
import android.view.MenuItem
import app.android.githubservice.R
import app.android.githubservice.base.BaseActivity
import app.android.githubservice.model.network.RetrofitInstance
import app.android.githubservice.repository.BaseRepository
import app.android.githubservice.repository.ReposRepository
import app.android.githubservice.ui.fragment.ReposFragment
import app.android.githubservice.ui.fragment.SearchFragment
import app.android.githubservice.ui.fragment.StarredFragment
import app.android.githubservice.viewmodel.RepositoriesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<RepositoriesViewModel>(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultFragment()
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun getRepository(): BaseRepository {
        return ReposRepository(RetrofitInstance.api)
    }

    override fun getViewModel() =
        RepositoriesViewModel::class.java

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.repositoriesFragment -> setFragments(R.id.container, ReposFragment().newInstance(), false)
            R.id.searchFragment -> setFragments(R.id.container, SearchFragment().newInstance(), false)
            R.id.starredFragment -> setFragments(R.id.container, StarredFragment().newInstance(), false)
        }
        return true
    }

    private fun setDefaultFragment() {
        setFragments(R.id.container, ReposFragment().newInstance(), false)
    }

}