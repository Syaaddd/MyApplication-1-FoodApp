package com.syadd.myapplication_1_foodapp.Helper

import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import java.util.concurrent.atomic.AtomicReference

/**
 * Kelas untuk mengelola keranjang belanja
 */
object CartManager {
    private val cartItems = AtomicReference(mutableListOf<FoodModel>())

    fun addToCart(food: FoodModel) {
        val currentCart = cartItems.get()
        val existingItem = currentCart.find { it.Id == food.Id }
        
        if (existingItem != null) {
            // Jika makanan sudah ada di keranjang, tambahkan jumlahnya
            existingItem.numberInCart++
        } else {
            // Jika makanan belum ada di keranjang, tambahkan sebagai item baru
            val foodCopy = food.copy()
            foodCopy.numberInCart = 1
            currentCart.add(foodCopy)
        }
        
        cartItems.set(currentCart)
    }

    fun removeFromCart(foodId: Int) {
        val currentCart = cartItems.get()
        currentCart.removeAll { it.Id == foodId }
        cartItems.set(currentCart)
    }

    fun updateQuantity(foodId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(foodId)
            return
        }
        
        val currentCart = cartItems.get()
        val existingItem = currentCart.find { it.Id == foodId }
        
        existingItem?.let {
            it.numberInCart = newQuantity
        }
        
        cartItems.set(currentCart)
    }

    fun getCartItems(): List<FoodModel> = cartItems.get().toList()

    fun getCartTotal(): Double {
        return cartItems.get().sumOf { it.Price * it.numberInCart }
    }

    fun getCartItemCount(): Int {
        return cartItems.get().sumOf { it.numberInCart }
    }

    fun clearCart() {
        cartItems.set(mutableListOf())
    }
}