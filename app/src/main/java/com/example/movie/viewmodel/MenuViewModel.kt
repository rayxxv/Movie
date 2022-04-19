package com.example.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    var username: MutableLiveData<String> = MutableLiveData("Hello, User")

}

