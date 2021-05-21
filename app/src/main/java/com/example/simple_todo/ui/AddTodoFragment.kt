package com.example.simple_todo.ui

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.example.simple_todo.R
import com.example.simple_todo.databinding.FragmentAddTodoBinding
import com.example.simple_todo.model.Todo
import com.example.simple_todo.utils.showToast
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.sign

@AndroidEntryPoint
class AddTodoFragment : Fragment(R.layout.fragment_add_todo) {

    private lateinit var binding: FragmentAddTodoBinding
    private var spinnerItem = arrayOf<String>()
    private var actionBar: ActionBar? = null
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAddTodoBinding.bind(view)

        setHasOptionsMenu(true)
        spinnerItem = resources.getStringArray(R.array.choice_priority)

        val spinnerAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerItem
        ) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                }
                return tv
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = spinnerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_todo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_save -> {
                val newTodo = Todo(
                    title = binding.etTitle.text.toString(),
                    description = binding.etDescription.text.toString(),
                    priority = binding.spinnerPriority.selectedItemPosition - 1,
                    createdAt = Calendar.getInstance().time
                )
                sharedViewModel.onSaveNewTodo(newTodo)
                    .observe(viewLifecycleOwner, { isSuccessSaved ->
                        if(isSuccessSaved) {
                            findNavController().navigateUp()
                        } else {
                            "Failed to insert new todo!".showToast(requireContext())
                        }
                    })
            }
        }
        return super.onOptionsItemSelected(item)
    }
}