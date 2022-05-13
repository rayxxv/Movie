package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.movie.datastore.DataStoreManager
import com.example.movie.room.User

class HomeViewModel(private val pref: DataStoreManager) : ViewModel() {

    suspend fun setDataUser(user: User){
        pref.setUser(user)
    }

    fun getDataUser(): LiveData<User>{
        return pref.getUser().asLiveData()
    }

}