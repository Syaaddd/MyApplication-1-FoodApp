package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.syadd.myapplication_1_foodapp.R

@Composable
fun OrderScreen(navController: NavController) {
    Scaffold(
        backgroundColor = colorResource(R.color.lightGrey), // ⬅️ background utama
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
                backgroundColor = Color.White, // ⬅️ warna AppBar
                elevation = 4.dp,
                modifier = Modifier
                    .statusBarsPadding() // ⬅️ Bikin AppBar agak turun dari status bar
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                // ❌ jangan pakai .background() di sini kalau sudah set di Scaffold
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
                        // Navigasi ke halaman utama
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .height(50.dp)
                        .width(200.dp), // biar simetris
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
    }
}
