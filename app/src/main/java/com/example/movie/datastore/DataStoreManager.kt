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

    companion object {

        private const val DATASTORE_NAME = "user_preferences"

        private val USERNAME_KEY = stringPreferencesKey("USERNAME")

        private val Context.userDataStore by preferencesDataStore(
            name = DATASTORE_NAME
        )

        const val DEFAULT_USERNAME = "DEF_USERNAME"
    }


    suspend fun saveUserPref(username: String) {
        context.userDataStore.edit {
            it[USERNAME_KEY] = username
        }
    }

    fun getUserPref(): Flow<String> {
        return context.userDataStore.data.map {
            it[USERNAME_KEY] ?: DEFAULT_USERNAME
        }
    }

    suspend fun deleteUserFromPref() {
        context.userDataStore.edit {
            it.clear()
        }
    }
}