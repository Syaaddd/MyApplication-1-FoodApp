package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.syadd.myapplication_1_foodapp.Activity.Admin.AdminAppNavigation

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreenWithNavigation(navController)
        }
        
        composable("food_detail/{foodId}") { backStackEntry ->
            // Mengambil ID makanan dari argumen
            val foodId = backStackEntry.arguments?.getString("foodId")?.toIntOrNull() ?: 0
            
            // Menampilkan halaman detail makanan
            FoodDetailScreenWithId(navController, foodId)
        }
        
        composable("cart") {
            CartScreen(navController)
        }
        
        composable("checkout") {
            CheckoutScreen(navController)
        }
        
        composable("favorite") {
            FavoriteScreen(navController)
        }
        
        composable("order") {
            OrderScreen(navController)
        }
        
        composable("profile") {
            ProfileScreen(navController)
        }
        
        composable("admin") {
            AdminAppNavigation()
        }
    }
}

@Composable
fun MainScreenWithNavigation(navController: NavHostController) {
    // Kita akan memanggil MainScreenWithNav yang didefinisikan di MainActivity.kt
    MainScreenWithNav(navController)
}