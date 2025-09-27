package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import com.syadd.myapplication_1_foodapp.Helper.FavoriteManager
import com.syadd.myapplication_1_foodapp.Helper.toRupiah
import com.syadd.myapplication_1_foodapp.R
import com.syadd.myapplication_1_foodapp.ViewModel.MainViewModel

@Composable
fun FavoriteScreen(navController: NavController) {
    val viewModel = MainViewModel()
    var favoriteFoods by remember { mutableStateOf<List<FoodModel>>(emptyList()) }
    
    // Load favorite foods whenever the screen recomposes
    LaunchedEffect(Unit) {
        val favoriteIds = FavoriteManager.getFavoriteFoodIds()
        favoriteFoods = emptyList() // Clear previous
        
        // For each favorite ID, load the corresponding food data
        val foods = mutableListOf<FoodModel>()
        favoriteIds.forEach { id ->
            viewModel.loadFoodDetail(id).observeForever { food ->
                if (food != null) {
                    foods.add(food)
                    favoriteFoods = foods.toList() // Update the state
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites", color = colorResource(R.color.darkPurple)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(R.color.darkPurple)
                        )
                    }
                },
                backgroundColor = Color.White
            )
        }
    ) { paddingValues ->
        if (favoriteFoods.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite foods yet",
                    fontSize = 18.sp,
                    color = colorResource(R.color.darkPurple)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorResource(R.color.lightGrey))
                    .padding(8.dp)
            ) {
                items(favoriteFoods) { food ->
                    FavoriteItem(
                        food = food,
                        onRemoveFromFavorite = { 
                            FavoriteManager.removeFromFavorites(food.Id)
                            // Update the list after removing
                            favoriteFoods = favoriteFoods.filter { it.Id != food.Id }
                        },
                        onClick = {
                            navController.navigate("food_detail/${food.Id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(
    food: FoodModel, 
    onRemoveFromFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
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
                
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Favorited",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            
            IconButton(
                onClick = onRemoveFromFavorite,
                modifier = Modifier
                    .size(36.dp)
                    .background(colorResource(R.color.lightGrey), shape = androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from favorites",
                    tint = Color.Red
                )
            }
        }
    }
}