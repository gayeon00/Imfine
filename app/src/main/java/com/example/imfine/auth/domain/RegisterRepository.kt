package com.example.imfine.auth.domain

import android.net.Uri

interface RegisterRepository {
    suspend fun registerUser(name: String, birthday: String, profile: Uri)

}
