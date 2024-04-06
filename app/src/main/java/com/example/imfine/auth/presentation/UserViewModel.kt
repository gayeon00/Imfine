package com.example.imfine.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.imfine.auth.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val userExists: LiveData<Boolean> = liveData {
        val userExists = userPreferences.userExists()
        emit(userExists)
    }
}