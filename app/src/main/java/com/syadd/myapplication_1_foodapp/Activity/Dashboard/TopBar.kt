package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syadd.myapplication_1_foodapp.R

@Composable
@Preview
fun TopBar() {
    Row(
        modifier = Modifier
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile photo (bulat)
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, CircleShape)
                .clickable {}
        )

        // Search Bar (tengah)
        var text by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    text = "What would you like to eat?",
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp)
                )
            },
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.DarkGray
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .height(55.dp)
        )

        Image(
            painter = painterResource(R.drawable.bell_icon),
            contentDescription = "Notifications",
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, CircleShape)
                .clickable {}
                .padding(8.dp)
        )
    }
}
