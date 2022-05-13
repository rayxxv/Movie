package com.example.movie.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.movie.room.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    suspend fun setUser(user: User){
        context.userDataStore.edit { preferences ->
            preferences[ID_USER] = user.id!!.toInt()
            preferences[USERNAME_KEY] = user.username
            preferences[EMAIL_KEY] = user.email
            preferences[PASSWORD_KEY] = user.password
        }
    }
    fun getUser(): Flow<User> {
        return context.userDataStore.data.map { preferences ->
            User(
                preferences[ID_USER] ?: -1,
                preferences[USERNAME_KEY] ?: "default_username",
                preferences[EMAIL_KEY] ?: "default_email@gmail.com",
                preferences[PASSWORD_KEY] ?: "default_password"
            )
        }
    }

    companion object {
        private const val DATASTORE_NAME = "user_preferences"
        private val ID_USER = intPreferencesKey("id_user_key")
        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val EMAIL_KEY = stringPreferencesKey("email_key")
        private val PASSWORD_KEY = stringPreferencesKey("password_key")
        val Context.userDataStore by preferencesDataStore(DATASTORE_NAME)
    }
}