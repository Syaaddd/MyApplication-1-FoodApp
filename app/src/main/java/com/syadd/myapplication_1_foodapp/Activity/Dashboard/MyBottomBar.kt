package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.syadd.myapplication_1_foodapp.R

@Composable
fun MyBottomBar(navController: NavController) {
    val bottomMenuItemsList = prepareBottomMenu()
    
    // Dapatkan route saat ini untuk menentukan item mana yang dipilih
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        backgroundColor = colorResource(R.color.white),
        elevation = 3.dp
    ) {
        bottomMenuItemsList.forEach { bottomMenuItem ->
            val isSelected = when (bottomMenuItem.lable) {
                "Home" -> currentRoute == "main"
                "Cart" -> currentRoute?.startsWith("cart") == true
                "Favorite" -> currentRoute?.startsWith("favorite") == true
                "Order" -> currentRoute?.startsWith("order") == true
                "Profile" -> currentRoute?.startsWith("profile") == true
                else -> false
            }
            
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    // Navigasi ke layar yang sesuai
                    when (bottomMenuItem.lable) {
                        "Home" -> navController.navigate("main") {
                            // Hapus semua route sebelumnya saat kembali ke home
                            popUpTo("main") { inclusive = true }
                        }
                        "Cart" -> navController.navigate("cart") {
                            // Tambahkan logika navigasi ke halaman keranjang
                        }
                        "Favorite" -> navController.navigate("favorite") {
                            // Tambahkan logika navigasi ke halaman favorit
                        }
                        "Order" -> navController.navigate("order") {
                            // Tambahkan logika navigasi ke halaman pesanan
                        }
                        "Profile" -> navController.navigate("profile") {
                            // Tambahkan logika navigasi ke halaman profil
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = bottomMenuItem.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(20.dp)
                    )
                }
            )
        }
    }
}

data class BottomMenuItem(
    val lable: String,
    val icon: Painter
)

@Composable
fun prepareBottomMenu(): List<BottomMenuItem> {
    return listOf(
        BottomMenuItem("Home", painterResource(R.drawable.btn_1)),
        BottomMenuItem("Cart", painterResource(R.drawable.btn_2)),
        BottomMenuItem("Favorite", painterResource(R.drawable.btn_3)),
        BottomMenuItem("Order", painterResource(R.drawable.btn_4)),
        BottomMenuItem("Profile", painterResource(R.drawable.btn_5))
    )
}

@Composable
@Preview
fun MyBottomBarPreview() {
    val navController = androidx.navigation.compose.rememberNavController()
    MyBottomBar(navController)
}
