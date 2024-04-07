package com.example.imfine.todolist.domain

import com.example.imfine.todolist.data.model.Todo

interface TodoRepository {
    suspend fun getAllTodos(): List<Todo>
    suspend fun getTodoById(id: Int): Todo?
    suspend fun insertTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodoById(id: Int)
}