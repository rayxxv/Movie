package com.example.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.room.User

class HomeViewModel: ViewModel() {

    val user: MutableLiveData<User> = MutableLiveData()

    fun getDataUser(data: User) {
        user.postValue(data)
    }

}