package com.syadd.myapplication_1_foodapp.Activity.DetailFood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.syadd.myapplication_1_foodapp.Domain.FoodModel
import com.syadd.myapplication_1_foodapp.Helper.previewFood
import com.syadd.myapplication_1_foodapp.R

class DetailEachFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

@Composable
fun DetailScreenPreview() {
    DetailScreen(
        item= previewFood,
        inBackClick={},
        onAddToCartClick={}
    )
}

@Composable
fun DetailScreen(item: FoodModel, inBackClick: () -> Unit, onAddToCartClick: () -> Unit) {
    var numberinCard by remember { mutableIntStateOf(item.numberInCart) }
    ConstraintLayout {
        val (footer, column) = createRefs()
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.lightGrey))
                    .verticalScroll(rememberScrollState())
                    .constrainAs(column){
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .padding(bottom = 100.dp)
        ){

        }
    }
}