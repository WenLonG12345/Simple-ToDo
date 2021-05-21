package com.example.simple_todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simple_todo.model.Todo

@Database(
    version = 1,
    entities = [
        Todo::class
    ],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoDB: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}