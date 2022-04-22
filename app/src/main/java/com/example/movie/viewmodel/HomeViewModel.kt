package com.example.movie.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.room.User
import com.example.movie.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    val user: MutableLiveData<User> = MutableLiveData()

    fun getDataUser(data: User) {
        user.postValue(data)
    }

}