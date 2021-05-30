package com.example.simple_todo.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "priority")
    val priority: Int,   // 0 - low, 1 - medium, 2 - high

    @ColumnInfo(name = "created_at")
    val createdAt: Date,

    @ColumnInfo(name = "is_task_done")
    val isTaskDone: Boolean = false


): Parcelable
