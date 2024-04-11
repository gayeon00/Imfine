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
        if (name.value!!.isBlank()) {
            _nameError.value = "Please enter your name."
        } else if (!name.value!!.matches(Regex("^[a-zA-Z]+\$"))) {
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
        if (birthday.value!!.isBlank()) {
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
        if (profileImageUri.value == null) {
            _profileImageUriError.value = "Please take a profile image."
        } else {
            _profileImageUriError.value = null
        }
    }

    fun registerUser() {
        viewModelScope.launch {
            isNameValid()
            isBirthdayValid()
            isUriValid()

            if (nameError.value == null && birthdayError.value == null && profileImageUriError.value == null) {
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