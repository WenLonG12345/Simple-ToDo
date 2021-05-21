package com.example.simple_todo.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.simple_todo.R
import com.example.simple_todo.databinding.FragmentTodoDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoDetailsFragment: DialogFragment(R.layout.fragment_todo_details) {

    private lateinit var binding: FragmentTodoDetailsBinding
    private val args by navArgs<TodoDetailsFragmentArgs>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTodoDetailsBinding.bind(view)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        args.todo?.let {
            with(binding) {
                tvTodoTitle.text = it.title
                tvTodoDescription.text = it.description

                when(it.priority) {
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
            }
        }

        binding.ivTodoDelete.setOnClickListener {
            args.todo?.let {
                sharedViewModel.onDeleteTodo(it)
                dialog?.dismiss()
            }
        }

    }
}