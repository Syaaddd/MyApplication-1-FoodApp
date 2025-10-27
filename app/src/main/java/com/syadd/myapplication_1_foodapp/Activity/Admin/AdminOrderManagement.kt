package com.syadd.myapplication_1_foodapp.Activity.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syadd.myapplication_1_foodapp.R
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*
import com.syadd.myapplication_1_foodapp.ViewModel.OrderViewModel
import com.syadd.myapplication_1_foodapp.Domain.OrderModel
import com.syadd.myapplication_1_foodapp.Domain.OrderItemModel

@Composable
fun AdminOrderManagementScreen(navController: NavHostController) {
    val viewModel = OrderViewModel()
    val scaffoldState = rememberScaffoldState()
    
    var orders by remember { mutableStateOf<List<OrderModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Load orders from Firebase
    LaunchedEffect(Unit) {
        viewModel.getAllOrders().observeForever { orderList ->
            orders = orderList
            isLoading = false
        }
    }
    
    Scaffold(
        topBar = {
            // ✅ Navbar lebih turun dan tetap bisa diklik
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 10.dp) // Geser ke bawah sedikit
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Order Management",
                            color = Color.White
                        )
                    },
                    backgroundColor = colorResource(R.color.darkPurple),
                    contentColor = Color.White,
                    elevation = 6.dp,
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorResource(R.color.lightGrey)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorResource(R.color.lightGrey)),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orders) { order ->
                    OrderCardFromFirebase(
                        order = order,
                        onStatusChange = { newStatus ->
                            viewModel.updateOrderStatus(order.orderId, newStatus)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCardFromFirebase(order: OrderModel, onStatusChange: (String) -> Unit) {
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

                val formatter = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault())
                val orderDate = formatter.format(Date(order.orderTime))
                
                Text(
                    text = orderDate,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Customer: ${order.customerName}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = order.customerAddress,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Order Items:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.darkPurple)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column {
                order.orderItems.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.foodName} x${item.quantity}",
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Rp ${String.format("%.0f", item.foodPrice * item.quantity)}",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total: Rp ${String.format("%.0f", order.totalAmount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkPurple)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChipFirebase(status = order.status)

                OrderActionButtonsFirebase(
                    currentStatus = order.status,
                    onStatusChange = onStatusChange
                )
            }
        }
    }
}



@Composable
fun StatusChipFirebase(status: String) {
    val statusColor = when (status) {
        "PENDING" -> Color.Gray
        "CONFIRMED" -> Color.Blue
        "PREPARING" -> Color.Yellow
        "READY" -> Color.Green
        "DELIVERED" -> Color.Cyan
        "CANCELLED" -> Color.Red
        else -> Color.Gray
    }

    val statusText = when (status) {
        "PENDING" -> "Pending"
        "CONFIRMED" -> "Confirmed"
        "PREPARING" -> "Preparing"
        "READY" -> "Ready"
        "DELIVERED" -> "Delivered"
        "CANCELLED" -> "Cancelled"
        else -> status
    }

    Card(
        backgroundColor = statusColor.copy(alpha = 0.2f),
        elevation = 0.dp
    ) {
        Text(
            text = statusText,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = statusColor,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}

@Composable
fun OrderActionButtonsFirebase(
    currentStatus: String,
    onStatusChange: (String) -> Unit
) {
    when (currentStatus) {
        "PENDING" -> {
            Row {
                Button(
                    onClick = { onStatusChange("CONFIRMED") },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Confirm",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onStatusChange("CANCELLED") },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel",
                        tint = Color.White
                    )
                }
            }
        }

        "CONFIRMED" -> {
            Button(
                onClick = { onStatusChange("PREPARING") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                modifier = Modifier.size(36.dp)
            ) {
                Text(">", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        "PREPARING" -> {
            Button(
                onClick = { onStatusChange("READY") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                modifier = Modifier.size(36.dp)
            ) {
                Text(">", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        "READY" -> {
            Button(
                onClick = { onStatusChange("DELIVERED") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Deliver",
                    tint = Color.White
                )
            }
        }

        else -> {
            Text("Completed", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
