package com.example.imfine.auth.presentation

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val birthday = MutableLiveData<String>()
    val uri = MutableLiveData<Uri?>()

    // 각 입력 필드의 유효성 검사 결과를 저장하는 LiveData
    val nameError = MutableLiveData<String?>()
    val birthdayError = MutableLiveData<String?>()
    val uriError = MutableLiveData<String?>()

    // LiveData를 사용해 '가입하기' 버튼 활성화 상태 관리
    val isRegisterEnabled = MediatorLiveData<Boolean>().apply {
        // nickname과 birthday의 변화를 관찰하고 유효성 검사 실행
        addSource(name) { validateForm() }
        addSource(birthday) { validateForm() }
        addSource(uri) { validateForm() }
    }

    private fun validateForm() {
        // 이름 유효성 검사
        nameError.value = if (name.value?.matches(Regex("^[a-zA-Z]+\$")) == true) null
        else "Invalid name. Only letters are allowed."

        // 생일 유효성 검사
        birthdayError.value = if (birthday.value?.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) == true) null
        else "Invalid date. Format should be YYYY-MM-DD."

        uriError.value = if (uri.value != null ) null else "Please select a profile image."

        isRegisterEnabled.value = nameError.value == null && birthdayError.value == null && uriError.value == null
    }
    fun setImageUrl(it: Uri?) {
        uri.value = it
    }
}