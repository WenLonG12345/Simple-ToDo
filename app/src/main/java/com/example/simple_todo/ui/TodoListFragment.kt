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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simple_todo.R
import com.example.simple_todo.databinding.FragmentTodoListBinding
import com.example.simple_todo.model.Todo
import com.example.simple_todo.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment: Fragment(R.layout.fragment_todo_list) {

    private lateinit var binding: FragmentTodoListBinding
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTodoListBinding.bind(view)
        setHasOptionsMenu(true)
        setupRecyclerView()

        sharedViewModel.allTodos.observe(viewLifecycleOwner, {
            binding.llEmptyList.isVisible = it.isEmpty()
            todoListAdapter.submitList(it)
        })


        binding.fabAddTodo.setOnClickListener {
            val action = TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment(null)
            findNavController().navigate(action)
        }

    }

    private fun setupRecyclerView() {
        todoListAdapter = TodoListAdapter { onTodoClick(it) }
        binding.rvTodo.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todoListAdapter
        }
    }

    private fun onTodoClick(todo: Todo) {
        val action = TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment(todo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_todo_list, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        searchView.onQueryTextChanged {
            sharedViewModel.searchQuery.value = it

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_sort_by_name -> {
                sharedViewModel.sortOrder.value = SortOrder.BY_TITLE
                true
            }

            R.id.action_sort_by_date_created -> {
                sharedViewModel.sortOrder.value = SortOrder.BY_DATE_CREATED
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}