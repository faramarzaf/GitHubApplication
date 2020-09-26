package app.android.githubservice.ui.fragment

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
import app.android.githubservice.ui.adapter.SavedAdapter
import app.android.githubservice.viewmodel.SearchViewModel
import app.android.githubservice.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : BaseFragment() {

    lateinit var viewModel: SearchViewModel
    lateinit var savedAdapter: SavedAdapter


    override val getFragmentLayout: Int
        get() = R.layout.fragment_favorite


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(SearchRepository(RetrofitInstance.api, GitHubDatabase(requireActivity())))
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
        setupRecyclerView()

        savedAdapter.setOnItemClickListener {
            toast(it.login)
        }



        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = savedAdapter.differ.currentList[position]
                viewModel.deleteUser(user)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveUser(user)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rv_saved)
        }

        viewModel.getAllUsers().observe(viewLifecycleOwner, Observer { articles ->
            savedAdapter.differ.submitList(articles)
        })

    }


    private fun setupRecyclerView() {
        savedAdapter = SavedAdapter()
        rv_saved.apply {
            adapter = savedAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}