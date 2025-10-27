package com.syadd.myapplication_1_foodapp.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.syadd.myapplication_1_foodapp.Domain.OrderItemModel
import com.syadd.myapplication_1_foodapp.Domain.OrderModel

class OrderRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val ordersReference = firebaseDatabase.getReference("Orders")

    fun placeOrder(order: OrderModel): LiveData<String> {
        val result = MutableLiveData<String>()
        val orderId = ordersReference.push().key ?: ""

        if (orderId.isNotEmpty()) {
            val orderWithId = order.copy(orderId = orderId)
            ordersReference.child(orderId).setValue(orderWithId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.value = "Order placed successfully with ID: $orderId"
                    } else {
                        result.value = "Failed to place order: ${task.exception?.message}"
                    }
                }
        } else {
            result.value = "Failed to generate order ID"
        }

        return result
    }

    fun getUserOrders(customerId: String): LiveData<MutableList<OrderModel>> {
        val ordersData = MutableLiveData<MutableList<OrderModel>>()
        val query = ordersReference.orderByChild("customerId").equalTo(customerId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ordersList = mutableListOf<OrderModel>()
                for (orderSnapshot in snapshot.children) {
                    val orderMap = orderSnapshot.getValue() as? Map<String, Any>
                    orderMap?.let {
                        val order = mapToOrderModel(it, orderSnapshot.key ?: "")
                        order?.let { ordersList.add(it) }
                    }
                }
                // Sort by order time (newest first)
                ordersList.sortByDescending { it.orderTime }
                ordersData.value = ordersList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return ordersData
    }

    fun getAllOrders(): LiveData<MutableList<OrderModel>> {
        val ordersData = MutableLiveData<MutableList<OrderModel>>()

        ordersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ordersList = mutableListOf<OrderModel>()
                for (orderSnapshot in snapshot.children) {
                    val orderMap = orderSnapshot.getValue() as? Map<String, Any>
                    orderMap?.let {
                        val order = mapToOrderModel(it, orderSnapshot.key ?: "")
                        order?.let { ordersList.add(it) }
                    }
                }
                // Sort by order time (newest first)
                ordersList.sortByDescending { it.orderTime }
                ordersData.value = ordersList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return ordersData
    }
    
    private fun mapToOrderModel(data: Map<String, Any>, orderId: String): OrderModel? {
        return try {
            OrderModel(
                orderId = orderId,
                customerId = data["customerId"] as? String ?: "",
                customerName = data["customerName"] as? String ?: "",
                customerPhone = data["customerPhone"] as? String ?: "",
                customerAddress = data["customerAddress"] as? String ?: "",
                orderItems = mapOrderItems(data["orderItems"] as? Map<String, Map<String, Any>>),
                totalAmount = (data["totalAmount"] as? Double ?: 0.0),
                status = data["status"] as? String ?: "PENDING",
                orderTime = (data["orderTime"] as? Long ?: System.currentTimeMillis())
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun mapOrderItems(itemsData: Map<String, Map<String, Any>>?): List<OrderItemModel> {
        val items = mutableListOf<OrderItemModel>()
        itemsData?.forEach { (_, itemData) ->
            try {
                val item = OrderItemModel(
                    foodId = (itemData["foodId"] as? Long ?: 0L).toInt(),
                    foodName = itemData["foodName"] as? String ?: "",
                    foodPrice = itemData["foodPrice"] as? Double ?: 0.0,
                    quantity = (itemData["quantity"] as? Long ?: 0L).toInt(),
                    imagePath = itemData["imagePath"] as? String ?: ""
                )
                items.add(item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return items
    }

    fun updateOrderStatus(orderId: String, newStatus: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        
        if (orderId.isNotEmpty()) {
            ordersReference.child(orderId).child("status").setValue(newStatus)
                .addOnCompleteListener { task ->
                    result.value = task.isSuccessful
                }
        } else {
            result.value = false
        }

        return result
    }

    fun addOrderItem(orderId: String, orderItem: OrderItemModel): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val itemRef = ordersReference.child(orderId).child("orderItems").push()

        itemRef.setValue(orderItem)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }

        return result
    }
}