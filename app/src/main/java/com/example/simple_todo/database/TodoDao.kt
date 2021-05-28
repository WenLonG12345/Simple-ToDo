package com.example.simple_todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.simple_todo.model.Todo
import com.example.simple_todo.ui.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Todo): Long

    @Query("SELECT * FROM todo WHERE title LIKE '%' || :searchQuery || '%'")
    fun listAllLiveData(searchQuery: String): LiveData<List<Todo>>

    fun getAllTodos(searchQuery: String, sortOrder: SortOrder) =
        when(sortOrder) {
            SortOrder.BY_TITLE -> getAllTodoSortedByTitle(searchQuery)
            SortOrder.BY_DATE_CREATED -> getAllTodoSortedByDateCreated(searchQuery)
        }

    @Query("SELECT * FROM todo WHERE title LIKE '%' || :searchQuery || '%' ORDER BY title")
    fun getAllTodoSortedByTitle(searchQuery: String): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE title LIKE '%' || :searchQuery || '%' ORDER BY created_at DESC")
    fun getAllTodoSortedByDateCreated(searchQuery: String): Flow<List<Todo>>

    @Delete
    fun delete(data: Todo)

    @Query("UPDATE todo SET  is_task_done = :isDone WHERE id = :id")
    fun setTodoIsDone(id: Int, isDone: Boolean)
}