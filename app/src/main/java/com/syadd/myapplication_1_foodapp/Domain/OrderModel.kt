package com.syadd.myapplication_1_foodapp.Domain

import com.google.firebase.database.PropertyName
import java.util.*

data class OrderModel(
    @PropertyName("orderId")
    val orderId: String = "",
    
    @PropertyName("customerId")
    val customerId: String = "",
    
    @PropertyName("customerName")
    val customerName: String = "",
    
    @PropertyName("customerPhone")
    val customerPhone: String = "",
    
    @PropertyName("customerAddress")
    val customerAddress: String = "",
    
    @PropertyName("orderItems")
    val orderItems: List<OrderItemModel> = listOf(),
    
    @PropertyName("totalAmount")
    val totalAmount: Double = 0.0,
    
    @PropertyName("status")
    val status: String = "PENDING", // PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED
    
    @PropertyName("orderTime")
    val orderTime: Long = System.currentTimeMillis(),
    
    @PropertyName("estimatedDeliveryTime")
    val estimatedDeliveryTime: Long = 0
)

data class OrderItemModel(
    @PropertyName("foodId")
    val foodId: Int = 0,
    
    @PropertyName("foodName")
    val foodName: String = "",
    
    @PropertyName("foodPrice")
    val foodPrice: Double = 0.0,
    
    @PropertyName("quantity")
    val quantity: Int = 0,
    
    @PropertyName("imagePath")
    val imagePath: String = ""
)