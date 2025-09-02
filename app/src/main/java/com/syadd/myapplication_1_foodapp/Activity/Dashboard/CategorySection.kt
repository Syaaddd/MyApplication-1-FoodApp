package com.syadd.myapplication_1_foodapp.Activity.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.syadd.myapplication_1_foodapp.Domain.CategoryModel
import com.syadd.myapplication_1_foodapp.R

@Composable
fun CategorySection(
    categories: SnapshotStateList<CategoryModel>, showCategoryLoading: Boolean
){
        if (showCategoryLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentAlignment = Alignment.Center

        ){
            CircularProgressIndicator()
        }
    } else {
        val rows=categories.chunked(3)
            val context= LocalContext.current

            Column(modifier = Modifier
                .fillMaxWidth()
            ) {
                rows.forEach {
                    row->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        row.forEachIndexed { index,categoryModel->
                            CategoryItem(
                                category = categoryModel,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp),
                                onItemClick = {

                                }
                            )
                        }
                        if(row.size>3){
                            repeat(3-row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
    }
}

@Composable
fun CategoryItem(category: CategoryModel
                 ,modifier: Modifier= Modifier,
                 onItemClick:()->Unit){
    Column (modifier=modifier
        .fillMaxWidth()
        .background(
            color = colorResource(R.color.white ),
            shape = RoundedCornerShape(13.dp)
        )
        .clickable(onClick = onItemClick)
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AsyncImage(
            model = category.ImagePath,
            contentDescription = null,
            modifier= Modifier.size(80.dp)
        )
        Text(
            text = category.Name,
            color = colorResource(R.color.darkPurple),
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}