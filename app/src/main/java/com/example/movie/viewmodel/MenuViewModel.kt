package com.example.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.room.User

class MenuViewModel : ViewModel() {
    var username: MutableLiveData<User> = MutableLiveData()
    fun getUser(user: User){
        username.postValue(user)
    }
}



