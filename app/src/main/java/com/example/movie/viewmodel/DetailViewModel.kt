package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movie.model.ResultX
import com.example.movie.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel {
    private var _detail = MutableLiveData<ResultX?>()
    val detail: LiveData<ResultX?> get() = _detail

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun getDetail(id: Int) {
        ApiClient.instance.getDetail(id).enqueue(object : Callback<ResultX> {
            override fun onResponse(call: Call<ResultX>, response: Response<ResultX>) {
                _loading.value = true
                when {
                    response.code() == 200 -> {
                        _loading.value = false
                        _detail.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<ResultX>, t: Throwable) {
                _loading.value = false
            }
        })
    }
}