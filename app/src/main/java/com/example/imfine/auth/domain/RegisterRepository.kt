package com.example.imfine.auth.domain

import android.net.Uri
import com.example.imfine.auth.data.model.Response

interface RegisterRepository {
    suspend fun registerUser(name: String, birthday: String, profile: Uri): Response<Unit>

}
