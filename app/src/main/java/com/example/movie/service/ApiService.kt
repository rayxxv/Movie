package com.example.movie.service

import com.example.movie.model.Popular
import com.example.movie.model.Result
import com.example.movie.model.ResultX
import com.example.movie.model.Tv
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular?api_key=$TOKEN")
    fun getAllMovie(): Call<Popular>

    @GET("movie/{movie_id}?api_key=$TOKEN")
    fun getMovieDetail(@Path("movie_id")movieid:Int): Call<Result>

    @GET("tv/popular?api_key=$TOKEN")
    fun getAllSeries(): Call<Tv>

    @GET("tv/{tv_id}?api_key=$TOKEN")
    fun getSeriesDetail(@Path("tv_id")tvid:Int): Call<ResultX>

    companion object {
        private const val TOKEN = "3dd2e30d36922558aeaa226cce93793d"
    }

}