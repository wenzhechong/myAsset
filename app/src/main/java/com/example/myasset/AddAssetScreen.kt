package com.example.myasset

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(modifier: Modifier = Modifier, onSaveClicked: () -> Unit ) {
    var name by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Available") }
    var lastUpdatedLocation by remember { mutableStateOf("") }
    var holder by remember { mutableStateOf("") }
    val serialIdToImageUriMap = remember { mutableStateMapOf<String, Uri>() }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    // Remember an ActivityResultLauncher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            Log.d("AddAssetScreen", "Image Uri received: $uri")
            uri?.let {
                if (serialNumber.isNotEmpty()) {
                    serialIdToImageUriMap[serialNumber] = it
                    imageUri = it // Update imageUri here
                    Log.d("AddAssetScreen", "Serial number is not empty, Uri mapped, imageUri updated")
                } else {
                    Log.e("AddAssetScreen", "Serial number is empty, cannot map Uri")
                }
            } ?: Log.e("AddAssetScreen", "Uri is null")
        }
    )

    Column(modifier = modifier.padding(10.dp)) {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Name", modifier = Modifier.weight(1f))
            TextField(value = name, onValueChange = { name = it }, modifier = Modifier.weight(1f))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Serial Number", modifier = Modifier.weight(1f))
            TextField(
                value = serialNumber,
                onValueChange = { serialNumber = it },
                modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Category", modifier = Modifier.weight(1f))
            TextField(
                value = category,
                onValueChange = { category = it },
                modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Current Location", modifier = Modifier.weight(1f))
            TextField(
                value = lastUpdatedLocation,
                onValueChange = { lastUpdatedLocation = it },
                modifier = Modifier.weight(1f)
            )
        }
        OutlinedButton(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Image")
        }
        // Display the picked image
        imageUri?.let { uri ->
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } ?: Text("Failed to load image", modifier = Modifier.padding(top = 10.dp))
        } ?: Text("No image selected", modifier = Modifier.padding(top = 10.dp))

        fun saveDataToFirebase() {
            val imageUriString = imageUri.toString()
            val assetData = AssetDataClass(
                name = name,
                serial = serialNumber,
                category = category,
                status = status,
                lastUpdatedLocation = lastUpdatedLocation,
                holder = holder,
                imageUri = imageUriString
                // Add other fields as needed
            )

            // Assuming you have a reference to your Firebase database, e.g., dataRef
            val dataRef = Firebase.database.getReference("assets")

            // Push the data to Firebase
            dataRef.push().setValue(assetData)
                .addOnSuccessListener {
                    Log.d("AddAssetScreen", "Data successfully saved")
                    onSaveClicked() // Call onSaveClicked after successful save
                }
                .addOnFailureListener { error ->
                    Log.e("AddAssetScreen", "Error saving data: ${error.message}")
                }
        }
        Button(
            onClick = {
                saveDataToFirebase()
                Log.d("AddAssetScreen", "Save button clicked")

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
