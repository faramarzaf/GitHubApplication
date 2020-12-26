package app.android.githubservice.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.model.db.GitHubDatabase
import app.android.githubservice.model.network.RetrofitInstance
import app.android.githubservice.repository.SearchRepository
import app.android.githubservice.ui.adapter.FavoriteAdapter
import app.android.githubservice.viewmodel.SearchViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.CallbackSnackBar
import com.faramarzaf.sdk.af_android_sdk.core.ui.SimpleSnackbar
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : BaseFragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter


    override val getFragmentLayout: Int
        get() = R.layout.fragment_favorite


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupRecyclerView()
        swipeRemoving(view)
        favoriteAdapter.setOnItemClickListener {
            toast(it.login)
        }
        observeUsersList()
    }


    private fun observeUsersList() {
        viewModel.getAllUsers().observe(viewLifecycleOwner, Observer { listFavorite ->
            favoriteAdapter.differ.submitList(listFavorite)
            if (listFavorite.isEmpty())
                noDataisAvailable()
            else
                dataisAvailable()
        })
    }

    private fun swipeRemoving(view: View) {

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = favoriteAdapter.differ.currentList[position]
                viewModel.deleteUser(user)
                SimpleSnackbar.show(view, "Successfully deleted user", "Undo", Color.GRAY,
                    Color.WHITE, Color.BLUE, true, object : CallbackSnackBar {
                        override fun onActionClick() {
                            viewModel.saveUser(user)
                        }
                    })
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rv_saved)
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory(SearchRepository(RetrofitInstance.api, GitHubDatabase(requireActivity())))
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter()
        rv_saved.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = favoriteAdapter
        }
    }

    fun noDataisAvailable() {
        textNoFavUser.visibility = View.VISIBLE
    }

    fun dataisAvailable() {
        textNoFavUser.visibility = View.GONE
    }
}