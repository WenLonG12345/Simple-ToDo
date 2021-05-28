package com.example.simple_todo.repository

import androidx.lifecycle.LiveData
import com.example.simple_todo.database.TodoDao
import com.example.simple_todo.model.Todo
import com.example.simple_todo.ui.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {


    fun getAllTodos(searchQuery: String, sortOrder: SortOrder): Flow<List<Todo>> {
        return todoDao.getAllTodos(searchQuery, sortOrder).flowOn(Dispatchers.IO)
    }

    fun getAllTodoLiveData(searchQuery: String): LiveData<List<Todo>> {
        return todoDao.listAllLiveData(searchQuery)
    }

    suspend fun saveNewTodo(todo: Todo): Long  {
         return withContext(Dispatchers.IO) {
             todoDao.insert(todo)
         }
    }

    suspend fun deleteTodo(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoDao.delete(todo)
        }
    }

    suspend fun updateTodoStatus(id: Int, isDone: Boolean) {
        withContext(Dispatchers.IO) {
            todoDao.setTodoIsDone(id, isDone)
        }
    }
}