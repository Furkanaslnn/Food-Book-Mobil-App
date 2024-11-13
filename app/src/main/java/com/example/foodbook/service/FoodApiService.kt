package com.example.foodbook.service

import com.example.foodbook.model.Food
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodApiService {

    private val retrofit = Retrofit.Builder().baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create()).build().create(FoodApi::class.java)

    suspend fun getFood(): List<Food> {
        return retrofit.getFood()
    }

}