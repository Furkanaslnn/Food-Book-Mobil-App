package com.example.foodbook.service

import com.example.foodbook.model.Food
import retrofit2.http.GET

interface FoodApi {

    // https://raw.githubusercontent.com/atilsamancioglu/BTK20-JSONVeriSeti/refs/heads/master/besinler.json
    // BASE URL -> https://raw.githubusercontent.com/
    // ENDPOINT -> atilsamancioglu/BTK20-JSONVeriSeti/refs/heads/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/refs/heads/master/besinler.json")
    suspend fun getFood(): List<Food>


}