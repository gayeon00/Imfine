package com.example.imfine.auth.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.imfine.auth.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.userPreferences: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreferences @Inject constructor(context: Context) {
    private val dataStore = context.userPreferences

    val user: Flow<User?>
        get() = dataStore.data.map { preferences ->
            val profileUri = preferences[KEY_PROFILE_URI]
            val name = preferences[KEY_NAME]
            val birthday = preferences[KEY_BIRTHDAY]
            if (profileUri != null && name != null && birthday != null) {
                User(profileUri, name, birthday)
            } else {
                null
            }
        }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[KEY_PROFILE_URI] = user.profileUri
            preferences[KEY_NAME] = user.name
            preferences[KEY_BIRTHDAY] = user.birthday
        }
    }

    suspend fun userExists(): Boolean {
        return user.first() != null
    }

    companion object {
        private val KEY_PROFILE_URI = stringPreferencesKey("profile_image")
        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_BIRTHDAY = stringPreferencesKey("birthday")
    }
}