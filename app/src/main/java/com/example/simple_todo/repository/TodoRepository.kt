package com.example.simple_todo.repository

import com.example.simple_todo.database.TodoDao
import com.example.simple_todo.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {

    val allTodos = todoDao.listAll()

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
}