package com.example.simple_todo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_todo.R
import com.example.simple_todo.databinding.FragmentTodoListBinding
import com.example.simple_todo.model.Todo
import com.example.simple_todo.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private lateinit var binding: FragmentTodoListBinding
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var todoListAdapter: TodoListAdapter

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTodoListBinding.bind(view)
        setHasOptionsMenu(true)
        setupRecyclerView()

        sharedViewModel.allTodos.observe(viewLifecycleOwner, {
            binding.llEmptyList.isVisible = it.isEmpty()
            todoListAdapter.submitList(it)
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel.todoEvent.collect { event ->
                when (event) {
                    is TodoEvent.ShowUndoDeleteMessage -> {
                        Snackbar.make(requireView(), "Todo deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                sharedViewModel.onUndoDelete(event.todo)
                            }.show()
                    }
                }
            }
        }

        binding.fabAddTodo.setOnClickListener {
            val action = TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment(null)
            findNavController().navigate(action)
        }

    }

    private fun setupRecyclerView() {
        todoListAdapter = TodoListAdapter { onTodoClick(it) }
        binding.rvTodo.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todoListAdapter
        }
        // swipe-to-do
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val todo = todoListAdapter.currentList[viewHolder.adapterPosition]
                sharedViewModel.onDeleteTodo(todo)
            }
        }).attachToRecyclerView(binding.rvTodo)
    }

    private fun onTodoClick(todo: Todo) {
        val action = TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment(todo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_todo_list, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
//        searchView.maxWidth = Int.MAX_VALUE

        searchView.onQueryTextChanged {
            sharedViewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                sharedViewModel.sortOrder.value = SortOrder.BY_TITLE
                true
            }

            R.id.action_sort_by_date_created -> {
                sharedViewModel.sortOrder.value = SortOrder.BY_DATE_CREATED
                true
            }

            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                sharedViewModel.hideCompleted.value = item.isChecked
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}