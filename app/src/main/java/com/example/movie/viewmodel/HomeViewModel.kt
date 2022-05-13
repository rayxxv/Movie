package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.movie.datastore.DataStoreManager
import com.example.movie.room.User
import kotlinx.coroutines.launch

class HomeViewModel(private val pref: DataStoreManager) : ViewModel() {

    fun saveDataStore(value: String) {
        viewModelScope.launch {
            pref.saveUserPref(value)
        }
    }

    fun getDataStore(): LiveData<String> {
        return pref.getUserPref().asLiveData()
    }

}