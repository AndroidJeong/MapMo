package com.jeong.mapmo.data.remote

import com.jeong.mapmo.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    inline fun <reified T> create(): T = retrofit.create(T::class.java)
}