package com.syadd.myapplication_1_foodapp.ViewModel

import androidx.lifecycle.LiveData
import com.syadd.myapplication_1_foodapp.Domain.OrderItemModel
import com.syadd.myapplication_1_foodapp.Domain.OrderModel
import com.syadd.myapplication_1_foodapp.Repository.OrderRepository

class OrderViewModel {
    private val repository = OrderRepository()

    fun placeOrder(order: OrderModel): LiveData<String> {
        return repository.placeOrder(order)
    }

    fun getUserOrders(customerId: String): LiveData<MutableList<OrderModel>> {
        return repository.getUserOrders(customerId)
    }

    fun getAllOrders(): LiveData<MutableList<OrderModel>> {
        return repository.getAllOrders()
    }

    fun updateOrderStatus(orderId: String, newStatus: String): LiveData<Boolean> {
        return repository.updateOrderStatus(orderId, newStatus)
    }

    fun addOrderItem(orderId: String, orderItem: OrderItemModel): LiveData<Boolean> {
        return repository.addOrderItem(orderId, orderItem)
    }
}