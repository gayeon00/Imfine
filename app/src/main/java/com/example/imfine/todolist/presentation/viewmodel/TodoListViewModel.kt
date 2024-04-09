package com.example.imfine.todolist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imfine.todolist.data.model.Todo
import com.example.imfine.todolist.domain.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
): ViewModel() {
    fun editTodo(item: Todo?) {
        Log.d("swipe edit", "$item")
    }

    fun completeTodo(item: Todo?) {
        Log.d("swipe complete", "$item")
    }

    private val _todoList = MutableLiveData<List<Todo>>()
    val todoList : LiveData<List<Todo>> = _todoList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getAllTodos().collect {
                _todoList.postValue(it)
            }
        }
    }

}