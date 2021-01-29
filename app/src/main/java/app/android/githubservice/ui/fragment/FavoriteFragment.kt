package app.android.githubservice.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.githubservice.R
import app.android.githubservice.base.BaseFragment
import app.android.githubservice.databinding.FragmentFavoriteBinding
import app.android.githubservice.ui.adapter.FavoriteAdapter
import app.android.githubservice.viewmodel.SearchViewModel
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.CallbackSnackBar
import com.faramarzaf.sdk.af_android_sdk.core.interfaces.DialogCallback
import com.faramarzaf.sdk.af_android_sdk.core.ui.SimpleSnackbar
import com.faramarzaf.sdk.af_android_sdk.core.ui.dialog.PublicDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*

@AndroidEntryPoint
class FavoriteFragment : BaseFragment() {

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: SearchViewModel by viewModels()

    override val getFragmentLayout: Int
        get() = R.layout.fragment_favorite


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        setupRecyclerView()
        swipeRemoving(view)
        observeUsersList()
        favoriteAdapter.setOnItemClickListener {
            toast(it.login)
        }
        binding.imgDeleteAll.setOnClickListener {
            deleteAllFavorites()
        }
    }

    private fun observeUsersList() {
        viewModel.getAllUsers().observe(viewLifecycleOwner, Observer { listFavorite ->
            favoriteAdapter.differ.submitList(listFavorite)
            if (listFavorite.isEmpty()) {
                binding.imgDeleteAll.isEnabled = false
                noDataAvailable()
            } else {
                binding.imgDeleteAll.isEnabled = true
                dataAvailable()
            }
        })
    }

    private fun deleteAllFavorites() {
        PublicDialog.yesNoDialog(requireContext(), getString(R.string.remove_all_title), getString(R.string.msg_dialog_remove_all)
            , getString(R.string.yes), getString(R.string.no), R.drawable.ic_delete, object : DialogCallback {
                override fun onNegativeButtonClicked() {
                    return
                }

                override fun onPositiveButtonClicked() {
                    viewModel.deleteAll()
                }
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
            attachToRecyclerView(rvSaved)
        }
    }


    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter()
        binding.rvSaved.apply {
            setRecyclerviewDivider(context, this, R.drawable.divider_list)
            layoutManager = LinearLayoutManager(activity)
            adapter = favoriteAdapter
        }
    }

    private fun noDataAvailable() {
        binding.textNoFavUser.visibility = View.VISIBLE
    }

    private fun dataAvailable() {
        binding.textNoFavUser.visibility = View.GONE
    }
}