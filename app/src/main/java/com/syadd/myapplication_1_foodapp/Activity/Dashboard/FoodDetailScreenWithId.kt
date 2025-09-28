package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import com.syadd.myapplication_1_foodapp.Helper.CartManager
import com.syadd.myapplication_1_foodapp.Helper.FavoriteManager
import com.syadd.myapplication_1_foodapp.Helper.previewFood
import com.syadd.myapplication_1_foodapp.ViewModel.MainViewModel

@Composable
fun FoodDetailScreenWithId(
    navController: NavController,
    foodId: Int
) {
    val viewModel = MainViewModel()
    var foodItem by remember { mutableStateOf<FoodModel?>(null) }
    var recommendations by remember { mutableStateOf<List<FoodModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isLoadingRecommendations by remember { mutableStateOf(true) }
    
    // For showing toast messages
    val context = LocalContext.current
    var toastMessage by remember { mutableStateOf<String?>(null) }
    
    // Mengambil data makanan dari Firebase
    LaunchedEffect(foodId) {
        if (foodId > 0) {
            viewModel.loadFoodDetail(foodId).observeForever { food ->
                foodItem = food
                isLoading = false
                
                // Setelah mendapatkan makanan, ambil rekomendasi berdasarkan kategori
                if (food.CategoryId.isNotEmpty()) {
                    viewModel.loadFiltered(food.CategoryId).observeForever { filteredList ->
                        // Ambil 3 makanan pertama sebagai rekomendasi, pastikan bukan makanan yang sedang dilihat
                        recommendations = filteredList.filter { it.Id != food.Id }.take(3)
                        isLoadingRecommendations = false
                    }
                } else {
                    isLoadingRecommendations = false
                }
            }
        } else {
            // Jika ID tidak valid, gunakan data dummy
            foodItem = previewFood
            isLoading = false
        }
    }
    
    // Handle toast messages
    toastMessage?.let { message ->
        LaunchedEffect(message) {
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
            toastMessage = null
        }
    }
    
    if (isLoading) {
        // Tampilkan indikator loading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(com.syadd.myapplication_1_foodapp.R.color.lightGrey)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colorResource(com.syadd.myapplication_1_foodapp.R.color.darkPurple)
            )
        }
    } else {
        foodItem?.let { food ->
            if (!isLoadingRecommendations) {
                FoodDetailScreen(
                    navController = navController,
                    foodItem = food,
                    recommendations = recommendations,
                    onFavoriteChange = { foodModel, isFavorite ->
                        if (isFavorite) {
                            FavoriteManager.addToFavorites(foodModel.Id)
                        } else {
                            FavoriteManager.removeFromFavorites(foodModel.Id)
                        }
                    },
                    onOrderClick = { orderedFood ->
                        CartManager.addToCart(orderedFood)
                        // Set toast message to be shown
                        toastMessage = "${orderedFood.Title} added to cart!"
                    }
                )
            } else {
                // Tampilkan loading saat mengambil rekomendasi
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(com.syadd.myapplication_1_foodapp.R.color.lightGrey)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(com.syadd.myapplication_1_foodapp.R.color.darkPurple)
                    )
                }
            }
        } ?: run {
            // Jika data makanan tidak ditemukan, tampilkan pesan error
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(com.syadd.myapplication_1_foodapp.R.color.lightGrey))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Food not found",
                    color = colorResource(com.syadd.myapplication_1_foodapp.R.color.darkPurple),
                    fontSize = 18.sp
                )
            }
        }
    }
}