package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.datastore.DataStoreManager
import com.example.movie.model.ResultX
import com.example.movie.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSeriesViewModel : ViewModel() {
    private val _detailSeries: MutableLiveData<ResultX> = MutableLiveData()
    val detailSeries: LiveData<ResultX> = _detailSeries

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getDetailSeries(seriesid: Int){
        ApiClient.instance.getSeriesDetail(seriesid).enqueue(object : Callback<ResultX> {
            override fun onResponse(
                call: Call<ResultX>,
                response: Response<ResultX>
            ) {
                when {
                    response.code() == 200 -> {
                        _loading.value = false
                        _detailSeries.value = response.body()
                    }
                }
            }
            override fun onFailure(call: Call<ResultX>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}