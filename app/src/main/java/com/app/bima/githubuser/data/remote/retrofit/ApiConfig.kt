package com.app.bima.githubuser.data.remote.retrofit

import com.app.bima.githubuser.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private val key = BuildConfig.KEY
        fun getApiService(): ApiService {
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeader = req.newBuilder()
                    .addHeader("Authorization", key)
                    .build()
                chain.proceed(requestHeader)
            }
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}