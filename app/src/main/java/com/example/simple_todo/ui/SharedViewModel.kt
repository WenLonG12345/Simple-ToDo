package com.example.simple_todo.ui

import androidx.lifecycle.*
import com.example.simple_todo.model.Todo
import com.example.simple_todo.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val todoRepository: TodoRepository
): ViewModel() {

    val allTodos = todoRepository.allTodos

    fun onSaveNewTodo(todo: Todo): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val res = todoRepository.saveNewTodo(todo)
            if(res.sign != -1) {
                result.postValue(true)
            } else {
                result.postValue(false)
            }
        }
        return result
    }

    fun onDeleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }

}