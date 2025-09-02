package com.syadd.myapplication_1_foodapp.ViewModel

import androidx.lifecycle.LiveData
import com.syadd.myapplication_1_foodapp.Domain.CategoryModel
import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import com.syadd.myapplication_1_foodapp.Repository.MainRepository

class MainViewModel {
    private val repository = MainRepository()

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }


    fun loadBestFood(): LiveData<MutableList<FoodModel>>{
        return repository.loadBestFood()
    }
}