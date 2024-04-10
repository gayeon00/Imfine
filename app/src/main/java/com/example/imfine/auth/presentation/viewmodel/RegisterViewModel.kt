package com.example.imfine.auth.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
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
    private val _name = MutableLiveData<String>("")
    val name: LiveData<String> = _name

    private val _birthday = MutableLiveData<String>("")
    val birthday: LiveData<String> = _birthday

    private val _profileImageUri = MutableLiveData<Uri?>(null)
    val profileImageUri: LiveData<Uri?> = _profileImageUri

    // 각 입력 필드의 유효성 검사 결과를 저장하는 LiveData
    private val _nameError = MutableLiveData<String?>(null)
    val nameError: LiveData<String?> = _nameError

    private val _birthdayError = MutableLiveData<String?>(null)
    val birthdayError: LiveData<String?> = _birthdayError

    private val _profileImageUriError = MutableLiveData<String?>(null)
    val profileImageUriError: LiveData<String?> = _profileImageUriError

    val saveResult: MutableLiveData<Response<Unit>> = MutableLiveData()

    fun setName(name: String) {
        _name.value = name
        isNameValid()
    }

    private fun isNameValid() {
        if (_name.value!!.isBlank()) {
            _nameError.value = "Please enter your name."
        } else if (!_name.value!!.matches(Regex("^[a-zA-Z]+\$"))) {
            _nameError.value = "Invalid name. Only letters are allowed."
        } else {
            _nameError.value = null
        }
    }

    fun setBirthday(birthday: String) {
        _birthday.value = birthday
        isBirthdayValid()
    }

    private fun isBirthdayValid() {
        if (_birthday.value!!.isBlank()) {
            _birthdayError.value = "Please select a birthday."
        } else {
            _birthdayError.value = null
        }
    }

    fun setUri(uri: Uri?) {
        _profileImageUri.value = uri
        isUriValid()
    }

    private fun isUriValid() {
        if (_profileImageUri.value == null) {
            _profileImageUriError.value = "Please take a profile image."
        } else {
            _profileImageUriError.value = null
        }
    }

//    // '가입하기' 버튼 활성화 상태 관리
//    val isRegisterEnabled = MediatorLiveData<Boolean>().apply {
//        // 입력 필드의 에러 메시지 LiveData를 소스로 추가
//        addSource(_nameError) { validateForm() }
//        addSource(_birthdayError) { validateForm() }
//        addSource(_profileImageUriError) { validateForm() }
//    }
//
//    private fun validateForm() {
//        // 모든 입력 필드가 유효할 때만 '가입하기' 버튼을 활성화
//        isRegisterEnabled.value =
//            listOf(_nameError, _birthdayError, _profileImageUriError).all { it.value == null }
//    }

//    fun validateName(text: String) {
//        _name.value = text
//        _nameError.value = if (name.value?.matches(Regex("^[a-zA-Z]+\$")) == true) null
//        else "Invalid name. Only letters are allowed."
//    }

//    fun validateBirthday(dateTime: String) {
//        _birthday.value = dateTime
//        _birthdayError.value =
//            if (birthday.value != null) null
//            else "Please select a birthday."
//    }
//
//    fun validateUri(uri: Uri?) {
//        this._profileImageUri.value = uri
//        _profileImageUriError.value =
//            if (this.profileImageUri.value != null) null else "Please select a profile image."
//    }

    fun registerUser() {
        viewModelScope.launch {
            isNameValid()
            isBirthdayValid()
            isUriValid()

            if (_nameError.value == null && _birthdayError.value == null && _profileImageUriError.value == null) {
                //유효성 검사에 통과하면 가입시키기
                val response =
                    registerRepository.registerUser(
                        name.value!!,
                        birthday.value!!,
                        profileImageUri.value!!
                    )

                //에러 메세지 초기화
                _nameError.value = null
                _birthdayError.value = null
                _profileImageUriError.value = null

                //가입 결과 반환
                saveResult.value = response
            }

        }
    }
}