package com.example.imfine.auth.data.repository

import android.net.Uri
import com.example.imfine.auth.data.local.UserDao
import com.example.imfine.auth.data.model.User
import com.example.imfine.auth.domain.RegisterRepository
import javax.inject.Inject


class RegisterRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : RegisterRepository {
    override suspend fun registerUser(name: String, birthday: String, profile: Uri) {
        val user = User(name = name, birthday = birthday, profileUri = profile.toString())
        userDao.insertUser(user)
    }
}