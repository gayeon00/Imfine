package com.example.imfine.auth.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import com.example.imfine.auth.data.local.UserPreferences
import com.example.imfine.auth.data.model.Response
import com.example.imfine.auth.data.model.User
import com.example.imfine.auth.domain.RegisterRepository
import javax.inject.Inject


class RegisterRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : RegisterRepository {
    override suspend fun registerUser(name: String, birthday: String, profile: Uri): Response<Unit> {
        val user = User(name = name, birthday = birthday, profileUri = profile.toString())
        userPreferences.saveUser(user)
        return Response.Success(Unit)
    }
}