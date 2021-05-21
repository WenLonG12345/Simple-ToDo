package com.example.simple_todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.simple_todo.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Todo): Long

    @Query("SELECT * FROM todo")
    fun listAll(): LiveData<List<Todo>>

    @Delete
    fun delete(data: Todo)
}