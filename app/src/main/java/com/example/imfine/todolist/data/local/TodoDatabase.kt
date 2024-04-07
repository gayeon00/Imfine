package com.example.imfine.todolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.imfine.todolist.data.model.Todo
import com.example.imfine.todolist.data.util.DateTimeConverters

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverters::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}