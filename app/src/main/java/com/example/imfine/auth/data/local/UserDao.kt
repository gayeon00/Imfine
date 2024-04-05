package com.example.imfine.auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import com.example.imfine.auth.data.model.User
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)
}