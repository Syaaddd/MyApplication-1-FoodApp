package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import com.syadd.myapplication_1_foodapp.Helper.CartManager
import com.syadd.myapplication_1_foodapp.Helper.FavoriteManager
import com.syadd.myapplication_1_foodapp.Helper.toRupiah
import com.syadd.myapplication_1_foodapp.R
import android.widget.Toast

@Composable
fun FoodDetailScreen(
    navController: NavController,
    foodItem: FoodModel,
    recommendations: List<FoodModel> = emptyList(),
    onFavoriteChange: (FoodModel, Boolean) -> Unit = { _, _ -> },
    onOrderClick: (FoodModel) -> Unit = { }
) {
    var isFavorite by remember(foodItem.Id) { mutableStateOf(FavoriteManager.isFavorite(foodItem.Id)) }
    
    // Update status favorit jika berubah
    LaunchedEffect(foodItem.Id) {
        isFavorite = FavoriteManager.isFavorite(foodItem.Id)
    }
    
    Scaffold(
        topBar = {
            // Header Section (1:43:49)
            FoodDetailHeader(
                onBackClick = { navController.popBackStack() },
                onFavoriteClick = { 
                    isFavorite = !isFavorite
                    onFavoriteChange(foodItem, isFavorite)
                },
                isFavorite = isFavorite
            )
        },
        bottomBar = {
            // Footer Section (2:30:48)
            FoodDetailFooter(
                price = foodItem.Price,
                onOrderClick = { onOrderClick(foodItem) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(R.color.lightGrey))
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                // Gambar makanan besar
                FoodDetailImage(foodItem)
                
                // Title Row (2:02:42)
                FoodDetailTitleRow(foodItem, isFavorite)
                
                // Food Detail Row (2:15:27)
                FoodDetailInfo(foodItem)
                
                // Description & Recommended List (2:20:58)
                FoodDescriptionAndRecommendations(foodItem, recommendations)
            }
        }
    }
}

@Composable
fun FoodDetailHeader(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp, 16.dp, 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tombol back
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.darkPurple),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = colorResource(R.color.darkPurple)
            )
        }
        
        // Tombol favorite
        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.darkPurple),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else colorResource(R.color.darkPurple)
            )
        }
    }
}

@Composable
fun FoodDetailImage(foodItem: FoodModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        AsyncImage(
            model = foodItem.ImagePath,
            contentDescription = foodItem.Title,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(0.dp)), // Remove rounding to show full image
            contentScale = ContentScale.Crop
        )
        
        // Overlay gradient untuk efek visual
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f,
                        endY = 600f
                    )
                )
        )
    }
}

@Composable
fun FoodDetailTitleRow(foodItem: FoodModel, isFavorite: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = foodItem.Title,
                color = colorResource(R.color.darkPurple),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color.Yellow,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = foodItem.Star.toString(),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
        
        Text(
            text = foodItem.Price.toRupiah(),
            color = colorResource(R.color.darkPurple),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun FoodDetailInfo(foodItem: FoodModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FoodDetailInfoItem("Size", "Medium")
        FoodDetailInfoItem("Calories", "${foodItem.Calorie} Kcal")
        FoodDetailInfoItem("Cooking", "${foodItem.TimeValue} mins")
    }
}

@Composable
fun FoodDetailInfoItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = colorResource(R.color.darkPurple),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun FoodDescriptionAndRecommendations(
    foodItem: FoodModel,
    recommendations: List<FoodModel> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Description",
            color = colorResource(R.color.darkPurple),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = foodItem.Description,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Text(
            text = "Recommendations",
            color = colorResource(R.color.darkPurple),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )
        
        // Daftar rekomendasi makanan
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (recommendations.isNotEmpty()) {
                items(recommendations) { food ->
                    RecommendedFoodItem(food = food)
                }
            } else {
                // Jika tidak ada rekomendasi, tampilkan item dummy
                items(3) { index ->
                    RecommendedFoodItem(
                        food = FoodModel(
                            Id = index,
                            Title = "Recommended Food $index",
                            Price = (10 + index * 5).toDouble(),
                            Star = 4.0 + index * 0.2,
                            ImagePath = "",
                            Description = "Delicious recommended food",
                            CategoryId = "",
                            BestFood = true,
                            Calorie = 200 + index * 50,
                            TimeValue = 20 + index * 5,
                            TimeId = 0,
                            LocationId = 0,
                            PriceId = 0
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedFoodItem(food: FoodModel) {
    val context = LocalContext.current
    val isFavorite = remember { mutableStateOf(FavoriteManager.isFavorite(food.Id)) }
    
    Column(
        modifier = Modifier
            .width(150.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Box {
            AsyncImage(
                model = food.ImagePath,
                contentDescription = food.Title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.profile), // Gunakan placeholder jika gambarnya belum dimuat
                error = painterResource(R.drawable.profile) // Gunakan placeholder jika gambarnya gagal dimuat
            )
            
            // Tombol favorit kecil di pojok kanan atas
            IconButton(
                onClick = {
                    val newFavoriteState = !isFavorite.value
                    isFavorite.value = newFavoriteState
                    if (newFavoriteState) {
                        FavoriteManager.addToFavorites(food.Id)
                    } else {
                        FavoriteManager.removeFromFavorites(food.Id)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(30.dp)
                    .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite.value) Color.Red else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Text(
            text = food.Title,
            color = colorResource(R.color.darkPurple),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = food.Price.toRupiah(),
                color = colorResource(R.color.darkPurple),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color.Yellow,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = food.Star.toString(),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
        
        Button(
            onClick = {
                CartManager.addToCart(food)
                // Tampilkan pesan bahwa makanan telah ditambahkan ke keranjang
                Toast.makeText(context, "${food.Title} added to cart", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(30.dp)
                .background(colorResource(R.color.darkPurple), shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(R.color.darkPurple),
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Add",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FoodDetailFooter(
    price: Double,
    onOrderClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total Price",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = price.toRupiah(),
                    color = colorResource(R.color.darkPurple),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Button(
            onClick = onOrderClick,
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
                .padding(start = 16.dp)
                .background(colorResource(R.color.darkPurple), shape = RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
            elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Place an Order",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        }
    }
}