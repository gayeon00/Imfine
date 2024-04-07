package com.example.imfine.todolist.presentation.viewmodel

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

    private val _todoList = MutableLiveData<List<Todo>>()
    val todoList : LiveData<List<Todo>> = _todoList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _todoList.postValue(todoRepository.getAllTodos())
        }
    }

}