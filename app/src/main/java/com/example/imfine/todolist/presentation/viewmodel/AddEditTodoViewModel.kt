package com.example.imfine.todolist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imfine.todolist.data.model.Todo
import com.example.imfine.todolist.domain.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {
    //edit일 경우를 위해
    private val _todo = MutableLiveData<Todo>()
    val todo: LiveData<Todo> = _todo

    val task = MutableLiveData<String>()
    val dateTime = MutableLiveData<LocalDateTime?>()

    val taskError = MutableLiveData<String?>()
    val dateTimeError = MutableLiveData<String?>()

    // '가입하기' 버튼 활성화 상태 관리
    val isAddEditEnabled = MediatorLiveData<Boolean>().apply {
        // 입력 필드의 에러 메시지 LiveData를 소스로 추가
        addSource(taskError) { validateForm() }
        addSource(dateTimeError) { validateForm() }
    }

    private fun validateForm() {
        // 모든 입력 필드가 유효할 때만 '가입하기' 버튼을 활성화
        isAddEditEnabled.value =
            listOf(taskError, dateTimeError).all { it.value == null }
    }

    fun validateTask(text: String) {
        task.value = text
        taskError.value =
            if (text.isNotEmpty()) null
            else "Please enter a task."
    }

    fun validateDateTime(dateTime: LocalDateTime?) {
        this.dateTime.value = dateTime
        dateTimeError.value =
            if (dateTime != null) null
            else "Please select a date and time."
    }

    fun addTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            val todo = Todo(task = task.value!!, dateTime = dateTime.value!!, isCompleted = false)
            todoRepository.insertTodo(todo)
        }

    }

    fun editTodo() {
    }
}