package com.example.simple_todo.ui

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simple_todo.R
import com.example.simple_todo.databinding.FragmentTodoDetailsBinding
import com.example.simple_todo.model.Todo
import com.example.simple_todo.utils.setWidthPercent
import com.example.simple_todo.utils.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoDetailsFragment: DialogFragment(R.layout.fragment_todo_details) {

    private lateinit var binding: FragmentTodoDetailsBinding
    private val args by navArgs<TodoDetailsFragmentArgs>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var selectedTodo: Todo? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTodoDetailsBinding.bind(view)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent(85)

        selectedTodo = args.todo

        selectedTodo?.let { todo ->
            with(binding) {
                tvTodoTitle.text = todo.title
                tvTodoDescription.text = todo.description

                when(todo.priority) {
                    0 -> {
                        ivPriority.setImageResource(R.drawable.ic_priority_low)
                        tvPriority.text = getString(R.string.priority_low)
                    }
                    1 -> {
                        ivPriority.setImageResource(R.drawable.ic_priority_medium)
                        tvPriority.text = getString(R.string.priority_normal)
                    }
                    2 -> {
                        ivPriority.setImageResource(R.drawable.ic_priority_high)
                        tvPriority.text = getString(R.string.priority_high)
                    }
                }

                if(todo.isTaskDone) {
                    ivTodoIsdone.setImageResource(R.drawable.ic_mark_undone)
                } else {
                    ivTodoIsdone.setImageResource(R.drawable.ic_mark_done)
                }

                binding.ivTodoDelete.setOnClickListener {
                    sharedViewModel.onDeleteTodo(todo)
                    dialog?.dismiss()
                }

                binding.ivTodoIsdone.setOnClickListener {
                    sharedViewModel.onUpdateTodoStatus(todo.id, !todo.isTaskDone)
                    if(todo.isTaskDone) {
                        "Mark as Undone".showToast(requireContext())
                    } else {
                        "Mark as Done".showToast(requireContext())
                    }
                    dialog?.dismiss()
                }

                binding.ivTodoEdit.setOnClickListener {
                    val action = TodoDetailsFragmentDirections.actionTodoDetailsFragmentToAddTodoFragment(todo)
                    findNavController().navigate(action)
                }
            }
        }

    }
}