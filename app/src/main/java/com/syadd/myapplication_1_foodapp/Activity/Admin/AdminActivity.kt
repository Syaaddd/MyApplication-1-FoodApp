package com.syadd.myapplication_1_foodapp.Activity.Admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syadd.myapplication_1_foodapp.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.syadd.myapplication_1_foodapp.Activity.Dashboard.MyBottomBar

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdminAppNavigation()
        }
    }
}

@Composable
fun AdminAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "admin_dashboard"
    ) {
        composable("admin_dashboard") {
            AdminDashboardScreen(navController)
        }
        
        composable("admin_orders") {
            AdminOrderManagementScreen(navController)
        }
    }
}

@Composable
fun AdminDashboardScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            // Bungkus TopAppBar di dalam Box biar padding tidak ganggu area klik
            Box(
                modifier = Modifier
                    .statusBarsPadding()   // ⬅️ dorong ke bawah mengikuti status bar
                    .padding(top = 8.dp)   // ⬅️ tambahan offset kecil
            ) {
                TopAppBar(
                    title = { Text("Admin Dashboard", color = Color.White) },
                    backgroundColor = colorResource(R.color.darkPurple),
                    contentColor = Color.White,
                    elevation = 4.dp,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(R.color.lightGrey)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatsCard(
                        title = "Total Orders",
                        value = "124",
                        modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                        title = "Pending",
                        value = "12",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                StatsCard(
                    title = "Completed",
                    value = "112",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                NavigationCard(
                    title = "Manage Orders",
                    description = "View and manage all incoming orders",
                    onClick = { navController.navigate("admin_orders") }
                )
            }

            item {
                NavigationCard(
                    title = "Manage Menu",
                    description = "Add, edit or remove food items",
                    onClick = { /* Handle menu management */ }
                )
            }

            item {
                NavigationCard(
                    title = "Manage Users",
                    description = "View and manage user accounts",
                    onClick = { /* Handle user management */ }
                )
            }
        }
    }
}

@Composable
fun StatsCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(80.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkPurple)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun NavigationCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkPurple)
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Navigate",
                modifier = Modifier.graphicsLayer(rotationZ = 180f)
            )
        }
    }
}