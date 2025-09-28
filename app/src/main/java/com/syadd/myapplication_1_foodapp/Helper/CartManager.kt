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
        val existingItemIndex = currentCart.indexOfFirst { it.Id == food.Id }
        
        if (existingItemIndex != -1) {
            // Jika makanan sudah ada di keranjang, tambahkan jumlahnya
            val updatedItem = currentCart[existingItemIndex].copy(numberInCart = currentCart[existingItemIndex].numberInCart + 1)
            currentCart[existingItemIndex] = updatedItem
        } else {
            // Jika makanan belum ada di keranjang, tambahkan sebagai item baru
            val foodWithQuantity = food.copy(numberInCart = 1)
            currentCart.add(foodWithQuantity)
        }
        
        cartItems.set(currentCart)
    }

    fun removeFromCart(foodId: Int) {
        val currentCart = cartItems.get()
        val updatedCart = currentCart.filter { it.Id != foodId }.toMutableList()
        cartItems.set(updatedCart)
    }

    fun updateQuantity(foodId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(foodId)
            return
        }
        
        val currentCart = cartItems.get()
        val existingItemIndex = currentCart.indexOfFirst { it.Id == foodId }
        
        if (existingItemIndex != -1) {
            val updatedItem = currentCart[existingItemIndex].copy(numberInCart = newQuantity)
            currentCart[existingItemIndex] = updatedItem
            cartItems.set(currentCart)
        }
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