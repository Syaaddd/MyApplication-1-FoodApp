package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.syadd.myapplication_1_foodapp.R

@Composable
fun ProfileScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.lightGrey)) // background abu-abu utama
    ) {
        Scaffold(
            backgroundColor = Color.Transparent, // biar gak nutup bg abu
            topBar = {
                // Box untuk membungkus TopAppBar biar bisa turun dan rounded
                Box(
                    modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp) // ⬅️ lebih turun dari sebelumnya
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                "Profile",
                                color = colorResource(R.color.darkPurple),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = colorResource(R.color.darkPurple)
                                )
                            }
                        },
                        backgroundColor = Color.Transparent, // biar transparan dan pakai bg putih dari Box
                        elevation = 0.dp,
                        modifier = Modifier.height(56.dp)
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile picture
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "John Doe",
                    color = colorResource(R.color.darkPurple),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "johndoe@example.com",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Profile options
                ProfileOption(title = "Edit Profile", onClick = { /* Handle edit profile */ })
                ProfileOption(title = "Settings", onClick = { /* Handle settings */ })
                ProfileOption(title = "Help & Support", onClick = { /* Handle help & support */ })
                ProfileOption(title = "About", onClick = { /* Handle about */ })
                
                // Admin access (only for development/testing purposes)
                ProfileOption(title = "Admin Panel", onClick = { 
                    navController.navigate("admin")
                })
            }
        }
    }
}

@Composable
fun ProfileOption(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp
    ) {
        Text(
            text = title,
            color = colorResource(R.color.darkPurple),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
