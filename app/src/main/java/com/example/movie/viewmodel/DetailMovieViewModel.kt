package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.model.Result
import com.example.movie.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel: ViewModel() {
    private val _detailMovie: MutableLiveData<Result> = MutableLiveData()
    val detailMovie: LiveData<Result> = _detailMovie

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getDetailMovies(movieid: Int){
        ApiClient.instance.getMovieDetail(movieid).enqueue(object : Callback<Result> {
            override fun onResponse(
                call: Call<Result>,
                response: Response<Result>) {
                when {
                    response.code() == 200 -> {
                        _loading.value = false
                        _detailMovie.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                _loading.value = false
            }
        })
    }
}


