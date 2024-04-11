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
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {
    //edit일 경우를 위해
    private val _todo = MutableLiveData<Todo>()
    val todo: LiveData<Todo> = _todo

    private val _task = MutableLiveData<String>("")
    val task: LiveData<String> = _task

    private val _dateTime = MutableLiveData<LocalDateTime?>(null)
    val dateTime: LiveData<LocalDateTime?> = _dateTime

    private val _taskError = MutableLiveData<String?>(null)
    val taskError: LiveData<String?> = _taskError

    private val _dateTimeError = MutableLiveData<String?>(null)
    val dateTimeError: LiveData<String?> = _dateTimeError

    fun setTodo(todo: Todo) {
        _todo.value = todo
    }

    fun setInitialTask(task: String) {
        _task.value = task
    }

    fun setInitialDateTime(dt: LocalDateTime?) {
        _dateTime.value = dt
    }
    fun setTask(task: String) {
        _task.value = task
        isTaskValid()
    }

    fun setDateTime(dt: LocalDateTime?) {
        _dateTime.value = dt
        isDateTimeValid()
    }

    private fun isDateTimeValid() {
        if (dateTime.value == null) {
            _dateTimeError.value = "Please select a date and time."
        } else {
            _dateTimeError.value = null
        }
    }

    private fun isTaskValid() {
        if (task.value!!.isBlank()) {
            _taskError.value = "Please enter a task."
        } else if (!task.value!!.matches(Regex("^[a-zA-Z]+\$"))) {
            _taskError.value = "Invalid name. Only letters are allowed."
        } else {
            _taskError.value = null
        }
    }

    fun addTodo(onAdded: () -> Unit) {

        //task, datetime이 유효성 검사에 만족하는지 확인하고
        //만족하지 않는다면 에러 메세지 띄워주기
        isTaskValid()
        isDateTimeValid()

        if (taskError.value == null && dateTimeError.value == null) {

            val todo = Todo(
                task = task.value!!,
                dateTime = dateTime.value!!,
                isCompleted = false)

            viewModelScope.launch(Dispatchers.IO) {

                todoRepository.insertTodo(todo)

                withContext(Dispatchers.Main) {
                    onAdded()
                }
            }
        }
    }

    fun updateTodo(onUpdated: () -> Unit) {

        isTaskValid()
        isDateTimeValid()

        if (taskError.value == null && dateTimeError.value == null) {

            val newTodo = Todo(
                id = todo.value!!.id,
                task = task.value!!,
                dateTime = dateTime.value!!,
                isCompleted = todo.value!!.isCompleted
            )

            viewModelScope.launch(Dispatchers.IO) {

                todoRepository.updateTodo(newTodo)

                withContext(Dispatchers.Main) {
                    onUpdated()
                }
            }
        }
    }
}