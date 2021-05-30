package com.example.simple_todo.ui

import androidx.lifecycle.*
import com.example.simple_todo.model.Todo
import com.example.simple_todo.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
//    private val todoFlow = searchQuery.flatMapLatest {
//        todoRepository.getAllTodosBySearch(it)
//    }

    val sortOrder = MutableStateFlow(SortOrder.BY_TITLE)
    val hideCompleted = MutableStateFlow(false)

    private val todoEventChannel = Channel<TodoEvent>()
    val todoEvent = todoEventChannel.receiveAsFlow()


    private val todoFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted,
    ) { query, sortOrder, hideCompleted ->
        Triple(query, sortOrder, hideCompleted)
    }.flatMapLatest {
        todoRepository.getAllTodos(it.first, it.second, it.third)
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
            todoEventChannel.send(TodoEvent.ShowUndoDeleteMessage(todo))
        }
    }

    fun onUpdateTodoStatus(id: Int, isDone: Boolean) {
        viewModelScope.launch {
            todoRepository.updateTodoStatus(id, isDone)
        }
    }

    fun onUndoDelete(todo: Todo) {
        viewModelScope.launch {
            todoRepository.saveNewTodo(todo)
        }
    }
}

sealed class TodoEvent{
    data class ShowUndoDeleteMessage(val todo: Todo): TodoEvent()
}


enum class SortOrder {
    BY_TITLE,
    BY_DATE_CREATED
}