package com.example.myasset

import android.graphics.fonts.Font
import android.graphics.fonts.FontFamily
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(modifier:Modifier=Modifier){
    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color(0xff0238e6),
            titleContentColor = Color(0xffffff),
        ),
        modifier = modifier,
        title = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 25.sp,
                    text = "Assets"
                )
                Text(
                    fontSize = 15.sp,
                    text = "ABC Sdn Bhd",

                )
            }
        }
    )
}

@Composable
fun NavigationBar() {
    val items = listOf("Home", "Assets", "Camera", "Employee", "Profile")
    var selectedItem by remember { mutableStateOf(3) } // Assuming 'Camera' is the selected item

    NavigationBar(
        containerColor = Color(0xff0238e6), // Dark theme background color
        contentColor = Color.White, // Icon and label color
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = null) }, // Replace with appropriate icons
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6200EE), // Purple color for the selected item
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color(0xFF6200EE),
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyAppBar()
    NavigationBar()
}