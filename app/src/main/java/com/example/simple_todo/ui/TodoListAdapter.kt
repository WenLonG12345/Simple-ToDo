package com.example.simple_todo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_todo.R
import com.example.simple_todo.databinding.ItemTodoBinding
import com.example.simple_todo.model.Todo

class TodoListAdapter(
    private val onClick: (Todo) -> Unit
): ListAdapter<Todo, TodoListAdapter.TodoListVH>(TodoDiffUtil()) {

    inner class TodoListVH(private val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            with(binding) {
                tvTodoTitle.text = todo.title
                tvTodoDescription.text = todo.description

                when(todo.priority) {
                    0 -> ivPriority.setImageResource(R.drawable.ic_priority_low)
                    1 -> ivPriority.setImageResource(R.drawable.ic_priority_medium)
                    2 -> ivPriority.setImageResource(R.drawable.ic_priority_high)
                }

                ivMarkDone.isVisible = todo.isTaskDone

                rlTodo.setOnClickListener {
                    onClick(todo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListVH {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoListVH(binding)
    }

    override fun onBindViewHolder(holder: TodoListVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class TodoDiffUtil: DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }

}