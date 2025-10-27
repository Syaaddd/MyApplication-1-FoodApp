package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.syadd.myapplication_1_foodapp.Helper.CartManager
import com.syadd.myapplication_1_foodapp.Helper.toRupiah
import com.syadd.myapplication_1_foodapp.R
import com.syadd.myapplication_1_foodapp.ViewModel.OrderViewModel
import com.syadd.myapplication_1_foodapp.Domain.OrderItemModel
import com.syadd.myapplication_1_foodapp.Domain.OrderModel
import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CheckoutScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel = OrderViewModel()
    val cartItems = CartManager.getCartItems()
    val totalAmount = CartManager.getCartTotal()
    
    var customerName by rememberSaveable { mutableStateOf("") }
    var customerPhone by rememberSaveable { mutableStateOf("") }
    var customerAddress by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var orderResult by remember { mutableStateOf<String?>(null) }
    
    // Get customer ID from Firebase Authentication - implement this properly
    // For now, using a placeholder; in a real app you would get the authenticated user's ID
    val firebaseAuth = FirebaseAuth.getInstance()
    val customerId = firebaseAuth.currentUser?.uid ?: "guest_user_${System.currentTimeMillis()}"
    
    // Handle order result
    LaunchedEffect(orderResult) {
        orderResult?.let { message ->
            if (message.startsWith("Order placed successfully")) {
                Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_LONG).show()
                // Clear cart after successful order
                CartManager.clearCart()
                // Navigate back to main screen
                navController.navigate("main") {
                    popUpTo("cart") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Failed to place order: $message", Toast.LENGTH_LONG).show()
            }
            orderResult = null // Reset the result
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", color = colorResource(R.color.darkPurple)) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(colorResource(R.color.lightGrey))
        ) {
            // Customer Information Section
            Text(
                text = "Customer Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkPurple),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp)
            )
            
            OutlinedTextField(
                value = customerPhone,
                onValueChange = { customerPhone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp)
            )
            
            OutlinedTextField(
                value = customerAddress,
                onValueChange = { customerAddress = it },
                label = { Text("Delivery Address") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                maxLines = 3
            )
            
            // Order Summary Section
            Text(
                text = "Order Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkPurple),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            cartItems.forEach { food ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${food.Title} x${food.numberInCart}",
                        color = Color.Black
                    )
                    Text(
                        text = (food.Price * food.numberInCart).toRupiah(),
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Total Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkPurple)
                )
                Text(
                    text = totalAmount.toRupiah(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkPurple)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val lifecycleOwner = LocalLifecycleOwner.current
            // Place Order Button
            Button(
                onClick = {
                    if (customerName.isBlank() || customerPhone.isBlank() || customerAddress.isBlank()) {
                        Toast.makeText(context, "Please fill in all customer information", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    
                    // Convert cart items to OrderItemModel
                    val orderItems = cartItems.map { food ->
                        OrderItemModel(
                            foodId = food.Id,
                            foodName = food.Title,
                            foodPrice = food.Price,
                            quantity = food.numberInCart,
                            imagePath = food.ImagePath
                        )
                    }
                    
                    // Create order model
                    val order = OrderModel(
                        customerId = customerId,
                        customerName = customerName,
                        customerPhone = customerPhone,
                        customerAddress = customerAddress,
                        orderItems = orderItems,
                        totalAmount = totalAmount,
                        status = "PENDING"
                    )
                    
                    // Place the order to Firebase
                    val result = viewModel.placeOrder(order)
                    result.observe(lifecycleOwner) { message ->
                        isLoading = false
                        orderResult = message
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Place Order",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}