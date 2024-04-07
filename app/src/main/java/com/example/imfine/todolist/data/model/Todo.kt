package com.example.imfine.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val task: String,
    val dateTime: LocalDateTime,
    val isCompleted: Boolean = false
)
