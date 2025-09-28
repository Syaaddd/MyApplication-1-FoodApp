package com.syadd.myapplication_1_foodapp.Activity.ItemsList

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.syadd.myapplication_1_foodapp.ViewModel.MainViewModel
import com.syadd.myapplication_1_foodapp.R
import com.syadd.myapplication_1_foodapp.Domain.FoodModel

class ItemsListActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        id=intent.getStringExtra("id")?:""
        title=intent.getStringExtra("title")?:""

        setContent {
            ItemsListScreen(
                title=title,
                onBackClick={finish()},
                viewModel=viewModel,
                id=id
            )
        }
    }

    @Composable
    private fun ItemsListScreen(
        title: String,
        onBackClick: () -> Unit,
        viewModel: MainViewModel,
        id: String
    ) {
        val items by viewModel.loadFiltered(id).observeAsState(emptyList())
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(id ) {
            viewModel.loadFiltered(id)
        }
        LaunchedEffect(items) {
            isLoading = items.isEmpty()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.lightGrey))
        ){
            ConstraintLayout(
                modifier = Modifier
                    .padding(top = 36.dp, start = 16.dp, end = 16.dp)
            ){
                val (backBtn, cartTxt) = createRefs()

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(cartTxt){centerTo(parent)},
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    text = title
                )
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable{
                            onBackClick()
                        }
                        .constrainAs (backBtn){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )
            }
            if (isLoading) {
                Box (
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }else{
                ItemsLIstWithNotification(items)
            }
        }
    }
    
    @Composable
    private fun ItemsLIstWithNotification(items: List<FoodModel>) {
        // For showing toast messages
        val context = LocalContext.current
        var toastMessage by remember { mutableStateOf<String?>(null) }
        
        // Handle toast messages
        toastMessage?.let { message ->
            LaunchedEffect(message) {
                android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                toastMessage = null
            }
        }
        
        ItemsLIst(
            items = items,
            onAddToCart = { food ->
                com.syadd.myapplication_1_foodapp.Helper.CartManager.addToCart(food)
                // Set toast message to be shown
                toastMessage = "${food.Title} added to cart!"
            }
        )
    }
}