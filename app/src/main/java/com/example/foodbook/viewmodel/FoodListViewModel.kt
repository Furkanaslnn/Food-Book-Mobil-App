package com.example.foodbook.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodbook.model.Food
import com.example.foodbook.rommdb.FoodDatabase
import com.example.foodbook.service.FoodApiService
import com.example.foodbook.util.PrivateSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodListViewModel(application: Application) : AndroidViewModel(application) {

    val foods = MutableLiveData<List<Food>>()
    val foodErrorMessage = MutableLiveData<Boolean>()
    val foodLoading = MutableLiveData<Boolean>()

    private val foodApiService = FoodApiService()
    private val privateSharedPreferences = PrivateSharedPreferences(getApplication())

    private val updateTime = 10 * 60 * 1000 * 1000 * 1000L

    fun refreshData() {
        val saveTime = privateSharedPreferences.getTime()

        if (saveTime != null && saveTime != 0L && System.nanoTime() - saveTime < updateTime) {
            getDataFromRoom()
        } else {
            getDataFromNet()
        }
    }

    fun refreshDataFromNet() {
        getDataFromNet()
    }

    private fun getDataFromRoom() {
        foodLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val foodList = FoodDatabase(getApplication()).foodDao().getAll()
            withContext(Dispatchers.Main) {
                foodLoading.value = false
                showFood(foodList)
                Toast.makeText(getApplication(),"From Room", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getDataFromNet() {
        foodLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val foodList = foodApiService.getFood()
            withContext(Dispatchers.Main) {
                foodLoading.value = false
                saveToRoom(foodList)
                Toast.makeText(getApplication(), "From Net", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showFood(foodList: List<Food>) {
        foods.value = foodList
        foodErrorMessage.value = false
        foodLoading.value = false
    }

    private fun saveToRoom(foodList: List<Food>) {
        viewModelScope.launch {
            val dao = FoodDatabase(getApplication()).foodDao()
            dao.deleteAll()
            val uuidList = dao.insertAll(*foodList.toTypedArray())
            var i = 0
            while (i < foodList.size) {
                foodList[i].uuid = uuidList[i].toInt()
                i++
            }
            showFood(foodList)
        }
        privateSharedPreferences.saveTime(System.nanoTime())
    }

}