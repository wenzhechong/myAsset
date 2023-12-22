package com.example.myasset

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


    @Composable
    fun AssetDetailScreen(
        modifier: Modifier = Modifier,
        serial: String,
        navController: NavController
    ) {
        var asset by remember { mutableStateOf<AssetDataClass?>(null) }
        val databaseReference = FirebaseDatabase.getInstance().getReference("assets")
        var dataLoaded by remember { mutableStateOf(false) }
        LaunchedEffect(serial) {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { assetSnapshot ->
                        val assetItem = assetSnapshot.getValue(AssetDataClass::class.java)
                        if (assetItem?.serial == serial) {
                            asset = assetItem
                            return
                        }
                    }
                    if (asset == null) {
                        Log.d("AssetDetailScreen", "No asset found with serial: $serial")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("AssetDetailScreen", "Failed to read value.", error.toException())
                }
            })
        }
        if (asset!=null) {

            Column(modifier = modifier.padding(10.dp)) {
                // Display asset properties
                asset?.let { nonNullableAsset ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(nonNullableAsset.imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Asset Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    // ... the rest of your code to display asset details ...
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Name", modifier = Modifier.weight(1f))
                    Text(asset?.name ?: "N/A", modifier = Modifier.weight(1f))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Serial Number", modifier = Modifier.weight(1f))
                    Text(asset?.serial ?: "N/A", modifier = Modifier.weight(1f))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Category", modifier = Modifier.weight(1f))
                    // Access the category property if available in your AssetDataClass
                    Text(asset?.category ?: "N/A", modifier = Modifier.weight(1f))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Current Location", modifier = Modifier.weight(1f))
                    // Access the current location property if available in your AssetDataClass
                    Text(asset?.lastUpdatedLocation ?: "N/A", modifier = Modifier.weight(1f))
                }
            }
        } else {
            // Handle the case where the asset data is still loading or not available
            // You can display a loading indicator or an error message here.
            Text("Loading asset data...")
        }
    }
