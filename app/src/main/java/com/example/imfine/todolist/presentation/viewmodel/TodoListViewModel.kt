package com.example.imfine.todolist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imfine.auth.data.local.UserPreferences
import com.example.imfine.auth.data.model.User
import com.example.imfine.todolist.data.model.Todo
import com.example.imfine.todolist.domain.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val userReferences: UserPreferences
): ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _todoList = MutableLiveData<List<Todo>>()
    val todoList : LiveData<List<Todo>> = _todoList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getAllTodos().collect {
                _todoList.postValue(it)
            }
        }

        viewModelScope.launch {
             userReferences.user.collect {
                _user.postValue(it)
            }
        }
    }

    fun completeTodo(item: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            val todo = Todo(item.id, item.task, item.dateTime, true)
            todoRepository.updateTodo(todo)
        }
    }
}