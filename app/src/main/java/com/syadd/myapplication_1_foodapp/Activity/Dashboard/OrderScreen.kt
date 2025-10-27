package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.syadd.myapplication_1_foodapp.R
import com.syadd.myapplication_1_foodapp.ViewModel.OrderViewModel
import com.syadd.myapplication_1_foodapp.Domain.OrderModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderScreen(navController: NavController) {
    val viewModel = OrderViewModel()
    // Get customer ID from Firebase Authentication
    val firebaseAuth = FirebaseAuth.getInstance()
    val customerId = firebaseAuth.currentUser?.uid ?: "guest_user_${System.currentTimeMillis()}"
    
    var orders by remember { mutableStateOf<List<OrderModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Load orders from Firebase
    LaunchedEffect(Unit) {
        viewModel.getUserOrders(customerId).observeForever { orderList ->
            orders = orderList
            isLoading = false
        }
    }
    
    Scaffold(
        backgroundColor = colorResource(R.color.lightGrey),
        topBar = {
            TopAppBar(
                title = { Text("My Orders", color = colorResource(R.color.darkPurple)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(R.color.darkPurple)
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 4.dp,
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(R.color.darkPurple))
            }
        } else if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Orders",
                        color = colorResource(R.color.darkPurple),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "You don't have any orders yet",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )

                    Button(
                        onClick = {
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .height(50.dp)
                            .width(200.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "Start Ordering",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                items(orders) { order ->
                    OrderItemCard(order = order)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(order: OrderModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Order Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.orderId.take(10).uppercase(), // Show first 10 chars of order ID
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkPurple)
                )
                
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val orderDate = formatter.format(Date(order.orderTime))
                
                Text(
                    text = orderDate,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status
            val statusColor = when (order.status) {
                "PENDING" -> Color.Gray
                "CONFIRMED" -> Color.Blue
                "PREPARING" -> Color.Yellow
                "READY" -> Color.Green
                "DELIVERED" -> Color.Cyan
                "CANCELLED" -> Color.Red
                else -> Color.Gray
            }
            
            val statusText = when (order.status) {
                "PENDING" -> "Pending"
                "CONFIRMED" -> "Confirmed"
                "PREPARING" -> "Preparing"
                "READY" -> "Ready"
                "DELIVERED" -> "Delivered"
                "CANCELLED" -> "Cancelled"
                else -> order.status
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                
                Text(
                    text = statusText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Total Amount
            Text(
                text = "Total: Rp ${String.format("%.0f", order.totalAmount)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkPurple)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Order Items Count
            Text(
                text = "Items: ${order.orderItems.size}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
