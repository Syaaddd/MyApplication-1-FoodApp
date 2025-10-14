package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import com.syadd.myapplication_1_foodapp.Helper.CartManager
import com.syadd.myapplication_1_foodapp.Helper.toRupiah
import com.syadd.myapplication_1_foodapp.R
import com.syadd.myapplication_1_foodapp.ViewModel.MainViewModel

@Composable
fun CartScreen(navController: NavController) {
    var cartItems by remember { mutableStateOf(CartManager.getCartItems()) }

    // Observe cart changes
    var cartVersion by remember { mutableStateOf(0) }
    LaunchedEffect(cartVersion) {
        cartItems = CartManager.getCartItems()
    }

    val refreshCart = { cartVersion++ }

    Scaffold(
        backgroundColor = colorResource(R.color.lightGrey), // ⬅️ warna latar belakang global
        topBar = {
            TopAppBar(
                title = { Text("Your Cart", color = colorResource(R.color.darkPurple)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(R.color.darkPurple)
                        )
                    }
                },
                backgroundColor = Color.White, // AppBar putih
                elevation = 4.dp,
                modifier = Modifier
                    .statusBarsPadding() // ⬅️ bikin turun sesuai status bar
            )
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your cart is empty",
                    fontSize = 18.sp,
                    color = colorResource(R.color.darkPurple)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                // ❌ hapus .background() di sini supaya tidak double layer
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(cartItems) { food ->
                        CartItem(food = food, onQuantityChange = { newQty ->
                            CartManager.updateQuantity(food.Id, newQty)
                            refreshCart()
                        })
                    }
                }

                // Bagian total dan checkout
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = CartManager.getCartTotal().toRupiah(),
                                color = colorResource(R.color.darkPurple),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                // Tambahkan logika checkout di sini
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .width(140.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.darkPurple)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "Checkout",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CartItem(food: FoodModel, onQuantityChange: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = food.ImagePath,
                contentDescription = food.Title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = food.Title,
                    color = colorResource(R.color.darkPurple),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = food.Price.toRupiah(),
                    color = colorResource(R.color.darkPurple),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Quantity controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 4.dp)
            ) {
                // + Button
                Button(
                    onClick = {
                        val newQty = food.numberInCart + 1
                        onQuantityChange(newQty)
                    },
                    modifier = Modifier.size(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Quantity text
                Text(
                    text = food.numberInCart.toString(),
                    color = colorResource(R.color.darkPurple),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                // - Button
                Button(
                    onClick = {
                        val newQty = food.numberInCart - 1
                        if (newQty > 0) {
                            // Kurangi quantity biasa
                            onQuantityChange(newQty)
                        } else {
                            // Jika 1 → hapus item dari cart
                            CartManager.removeFromCart(food.Id)
                            onQuantityChange(0)
                        }
                    },
                    modifier = Modifier.size(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("-", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Total price
                Text(
                    text = (food.Price * food.numberInCart).toRupiah(),
                    color = colorResource(R.color.darkPurple),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}
