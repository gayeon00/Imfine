package com.example.imfine.auth.presentation

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imfine.auth.data.model.Response
import com.example.imfine.auth.domain.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {
    val name = MutableLiveData<String>()
    val birthday = MutableLiveData<String>()
    val uri = MutableLiveData<Uri?>()

    // 각 입력 필드의 유효성 검사 결과를 저장하는 LiveData
    val nameError = MutableLiveData<String?>()
    val birthdayError = MutableLiveData<String?>()
    val uriError = MutableLiveData<String?>()

    val saveResult: MutableLiveData<Response<Unit>> = MutableLiveData()

    // '가입하기' 버튼 활성화 상태 관리
    val isRegisterEnabled = MediatorLiveData<Boolean>().apply {
        // 입력 필드의 에러 메시지 LiveData를 소스로 추가
        addSource(nameError) { validateForm() }
        addSource(birthdayError) { validateForm() }
        addSource(uriError) { validateForm() }
    }

    private fun validateForm() {
        // 모든 입력 필드가 유효할 때만 '가입하기' 버튼을 활성화
        isRegisterEnabled.value =
            listOf(nameError, birthdayError, uriError).all { it.value == null }
    }

    fun validateName(text: String) {
        name.value = text
        nameError.value = if (name.value?.matches(Regex("^[a-zA-Z]+\$")) == true) null
        else "Invalid name. Only letters are allowed."
    }

    fun validateBirthday(text: String) {
        birthday.value = text
        birthdayError.value =
            if (birthday.value?.matches(Regex("\\d{4}.\\d{2}.\\d{2}")) == true) null
            else "Invalid date. Format should be YYYY.MM.DD."
    }

    fun validateUri(uri: Uri?) {
        this.uri.value = uri
        uriError.value = if (this.uri.value != null) null else "Please select a profile image."
    }

    fun registerUser() {
        viewModelScope.launch {
            val response =
                registerRepository.registerUser(name.value!!, birthday.value!!, uri.value!!)
            saveResult.value = response
        }
    }
}