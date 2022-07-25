package com.devmasterteam.tasks.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(){

    companion object{
        private lateinit var INSTANCE: Retrofit
        private fun getRetrofitInstance(): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
            if(!::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class){
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroid/API/")
                        .client(okHttpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }
    }
}