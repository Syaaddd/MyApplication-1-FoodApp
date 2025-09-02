package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.syadd.myapplication_1_foodapp.R

@Composable
@Preview
fun MyBottomBar() {
    val bottomMenuItemsList = prepareBottomMenu()
    val context = LocalContext.current
    val selectedItem by remember { mutableStateOf("Home") }

    BottomAppBar(
        backgroundColor = colorResource(R.color.white),
        elevation = 3.dp
    ) {
        bottomMenuItemsList.forEach { bottomMenuItem ->
            BottomNavigationItem(
                selected = (selectedItem == bottomMenuItem.lable),
                onClick = {
                    // TODO: handle navigation here
                },
                icon = {
                    Icon(
                        painter = bottomMenuItem.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top =8.dp)
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
