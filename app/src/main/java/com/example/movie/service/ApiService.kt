package com.example.movie.service

import com.example.movie.model.Popular
import com.example.movie.model.Tv
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("movie/popular?api_key=3dd2e30d36922558aeaa226cce93793d")
    fun getAllMovie(): Call<Popular>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id")movieid:Int): Popular

    @GET("tv/popular?api_key=3dd2e30d36922558aeaa226cce93793d")
    fun getAllSeries(): Call<Tv>

    @GET("tv/{tv_id}")
    fun getSeriesDetail(@Path("tv_id")tvid:Int): Tv

}