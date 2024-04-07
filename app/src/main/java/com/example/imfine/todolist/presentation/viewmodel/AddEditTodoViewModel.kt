package com.example.imfine.todolist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imfine.todolist.data.model.Todo
import com.example.imfine.todolist.domain.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
): ViewModel() {
    private val _todo = MutableLiveData<Todo>()
    val todo: LiveData<Todo> = _todo
    //edit일 경우를 위해 todo 객체 저장하고
    //title
    //date 저장

    //init에서 add인지 edit인지 식별해줌
}