package com.example.myasset

import SearchView
import android.content.res.AssetManager
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun AssetRow(asset: AssetDataClass, navController: NavController) {
        Card(
            modifier = Modifier.padding(8.dp)
                .clickable {
                    navController.navigate("assetDetail/${asset.serial}")
                },
        border = BorderStroke(width = 1.dp, color = Color.Gray)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {
                    // Replace with actual image
            AsyncImage(
                model = asset.imageUri, // Directly using the URI string
                contentDescription = "Asset Image",
                modifier = Modifier.size(90.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    asset.name,
                    fontSize = 25.sp,
                )
                Text(asset.serial)
                Text(asset.status)
            }
        }
    }

}



@Composable
fun AssetScreen( modifier: Modifier = Modifier,navController :NavController) {
    var assets by remember { mutableStateOf<List<AssetDataClass>>(emptyList()) }
    val databaseReference = FirebaseDatabase.getInstance().getReference("assets")
    LaunchedEffect(Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedAssets = mutableListOf<AssetDataClass>()
                for (assetSnapshot in snapshot.children) {

                    val asset = assetSnapshot.getValue(AssetDataClass::class.java)
                    asset?.let { fetchedAssets.add(it) }
                }
                assets = fetchedAssets

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AssetScreen", "Failed to read value.", error.toException())
            }
        })
    }
    val searchState = remember { mutableStateOf(TextFieldValue("")) }
    val filteredAssets =
        assets.filter { it.name.contains(searchState.value.text, ignoreCase = true) }

    Column(modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    "Asset List",
                    color = Color(0xFFADD8E6),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Serif
                )
            }
        }
        SearchView(state = searchState)
        LazyColumn {
            items(filteredAssets) { asset ->
                AssetRow(asset, navController)
               }
            }
        }
    }






