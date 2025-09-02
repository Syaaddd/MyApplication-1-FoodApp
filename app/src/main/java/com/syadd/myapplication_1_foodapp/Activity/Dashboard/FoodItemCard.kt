package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import com.syadd.myapplication_1_foodapp.Helper.previewFood
import com.syadd.myapplication_1_foodapp.R
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
@Preview
fun FoodItemCardGridPreview() {
    FoodItemCardGrid(item = previewFood)
}

@Composable
fun FoodItemCardGrid(item: FoodModel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(colorResource(R.color.white), shape = RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .clickable{},
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = item.ImagePath,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Text(text=item.Title,
            color = colorResource(R.color.darkPurple),
            fontSize=14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
    }
}
