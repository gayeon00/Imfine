package com.example.imfine.auth.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val name: String,
    val birthday: String, // 'yyyy.MM.dd' 형식이라 가정
    val profileUri: String // 프로필 이미지 URI
)
