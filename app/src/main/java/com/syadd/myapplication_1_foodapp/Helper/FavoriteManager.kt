package com.syadd.myapplication_1_foodapp.Helper

import java.util.concurrent.atomic.AtomicReference

/**
 * Kelas untuk mengelola makanan favorit
 */
object FavoriteManager {
    private val favoriteFoodIds = AtomicReference(mutableSetOf<Int>())

    fun addToFavorites(foodId: Int) {
        val currentFavorites = favoriteFoodIds.get()
        currentFavorites.add(foodId)
        favoriteFoodIds.set(currentFavorites)
    }

    fun removeFromFavorites(foodId: Int) {
        val currentFavorites = favoriteFoodIds.get()
        currentFavorites.remove(foodId)
        favoriteFoodIds.set(currentFavorites)
    }

    fun isFavorite(foodId: Int): Boolean {
        return favoriteFoodIds.get().contains(foodId)
    }

    fun getFavoriteFoodIds(): Set<Int> = favoriteFoodIds.get().toSet()
    
    fun toggleFavorite(foodId: Int): Boolean {
        val currentFavorites = favoriteFoodIds.get()
        val isFavoriteNow = if (currentFavorites.contains(foodId)) {
            currentFavorites.remove(foodId)
            false
        } else {
            currentFavorites.add(foodId)
            true
        }
        
        favoriteFoodIds.set(currentFavorites)
        return isFavoriteNow
    }
}