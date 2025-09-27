import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.syadd.myapplication_1_foodapp.Helper.CartManager
import com.syadd.myapplication_1_foodapp.Helper.toRupiah
import com.syadd.myapplication_1_foodapp.R
import com.syadd.myapplication_1_foodapp.ViewModel.MainViewModel

@Composable
fun CartScreen(navController: NavController) {
    var cartItems by remember { mutableStateOf(CartManager.getCartItems()) }
    
    // Update cart items whenever there are changes
    LaunchedEffect(Unit) {
        // We would typically use a state holder here to listen for cart changes
        // For simplicity, we'll just update on composition
        cartItems = CartManager.getCartItems()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart", color = colorResource(R.color.darkPurple)) },
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
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your cart is empty",
                    fontSize = 18.sp,
                    color = colorResource(R.color.darkPurple)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorResource(R.color.lightGrey))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(cartItems) { food ->
                        CartItem(food = food, onQuantityChange = { newQty ->
                            CartManager.updateQuantity(food.Id, newQty)
                            cartItems = CartManager.getCartItems() // Update the displayed list
                        })
                    }
                }
                
                // Total and checkout section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = CartManager.getCartTotal().toRupiah(),
                                color = colorResource(R.color.darkPurple),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Button(
                onClick = {
                    // Di sini Anda bisa menambahkan logika untuk checkout
                },
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(start = 16.dp)
                    .background(colorResource(R.color.darkPurple), shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
                elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Checkout",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(food: FoodModel, onQuantityChange: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quantity controls
                Button(
                    onClick = { 
                        val newQty = food.numberInCart - 1
                        onQuantityChange(newQty)
                    },
                    modifier = Modifier.size(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("-", fontSize = 14.sp)
                }
                
                Text(
                    text = food.numberInCart.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 16.sp
                )
                
                Button(
                    onClick = { 
                        val newQty = food.numberInCart + 1
                        onQuantityChange(newQty)
                    },
                    modifier = Modifier.size(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.darkPurple)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+", fontSize = 14.sp)
                }
                
                Text(
                    text = (food.Price * food.numberInCart).toRupiah(),
                    color = colorResource(R.color.darkPurple),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}