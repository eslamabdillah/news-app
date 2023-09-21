package com.example.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    companion object {
        private var retrofit: Retrofit? = null
        fun getInstance(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }
            return retrofit!!
        }

        fun getApi(): WebServices {
            return getInstance().create(WebServices::class.java)
        }
    }

}