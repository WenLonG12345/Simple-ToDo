package com.example.simple_todo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.simple_todo.R
import com.example.simple_todo.databinding.FragmentTodoListBinding
import com.example.simple_todo.model.Todo
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment: Fragment(R.layout.fragment_todo_list) {

    private lateinit var binding: FragmentTodoListBinding
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTodoListBinding.bind(view)

        setupRecyclerView()

        sharedViewModel.allTodos.observe(viewLifecycleOwner, {
            todoListAdapter.submitList(it)
        })


        binding.fabAddTodo.setOnClickListener {
            val action = TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment()
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
}