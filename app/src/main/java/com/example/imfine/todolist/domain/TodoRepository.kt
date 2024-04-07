package com.example.imfine.todolist.domain

import com.example.imfine.todolist.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodos(): Flow<List<Todo>>
    suspend fun getTodoById(id: Int): Todo?
    suspend fun insertTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodoById(id: Int)
}