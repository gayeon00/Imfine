package com.example.imfine.auth.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "birthday") val birthday: String, // 'yyyy-MM-dd' 형식이라 가정
    @ColumnInfo(name = "profileUri") val profileUri: String // 프로필 이미지 URI
)
