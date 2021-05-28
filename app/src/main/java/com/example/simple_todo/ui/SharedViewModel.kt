package com.example.simple_todo.ui

import androidx.lifecycle.*
import com.example.simple_todo.model.Todo
import com.example.simple_todo.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    val query = MutableLiveData("")
    private val todoLiveData = query.switchMap {
        todoRepository.getAllTodoLiveData(it)
    }

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_TITLE)

    private val todoFlow = combine(
        searchQuery,
        sortOrder
    ) { query, sortOrder ->
        Pair(query, sortOrder)
    }.flatMapLatest {
        todoRepository.getAllTodos(it.first, it.second)
    }

    val allTodos = todoFlow.asLiveData(viewModelScope.coroutineContext)

    fun onSaveNewTodo(todo: Todo): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val res = todoRepository.saveNewTodo(todo)
            if (res.sign != -1) {
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

    fun onUpdateTodoStatus(id: Int, isDone: Boolean) {
        viewModelScope.launch {
            todoRepository.updateTodoStatus(id, isDone)
        }
    }
}


enum class SortOrder {
    BY_TITLE,
    BY_DATE_CREATED
}