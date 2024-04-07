package com.example.imfine.todolist.data.repository

import com.example.imfine.todolist.data.local.TodoDao
import com.example.imfine.todolist.data.model.Todo
import com.example.imfine.todolist.domain.TodoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
): TodoRepository {
    override suspend fun getAllTodos(): List<Todo> {
        return todoDao.getAllTodos()
    }

    override suspend fun getTodoById(id: Int): Todo {
        return todoDao.getTodoById(id)
    }

    override suspend fun insertTodo(todo: Todo) {
        todoDao.insertTodo(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    override suspend fun deleteTodoById(id: Int) {
        todoDao.deleteTodoById(id)
    }
}