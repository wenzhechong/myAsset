package com.example.myasset

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import com.example.myasset.ui.theme.MyAssetTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.database.database


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Firebase.database
        val dataRef = database.getReference("/Asset")
        setContent {
            MyAssetTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { MyAppBar() },
                    bottomBar = { NavigationBar() },
                    floatingActionButton = {
                        // Determine the current screen
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        // Update FAB based on the current screen
                        when (currentRoute) {
                            "asset" -> FloatingActionButton(
                                onClick = {
                                    navController.navigate("addAsset")
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }



                        }
                    }
                ) { innerPadding ->
                    NavHost(navController, startDestination = "asset") {
                        composable("asset") { AssetScreen(modifier= Modifier.padding(innerPadding),navController) }
                        composable("addAsset") { AddAssetScreen(
                            modifier= Modifier.padding(innerPadding),
                            onSaveClicked = {
                                navController.popBackStack()
                            }
                         ) }
                        composable("assetDetail/{asset.serial}", arguments = listOf(navArgument("asset.serial") {
                            type = NavType.StringType })) { backStackEntry ->
                            // Retrieve the serial from the backStackEntry
                            val serial = backStackEntry.arguments?.getString("asset.serial") ?: ""
                            // Assuming you have an AssetDetailScreen that takes a serial as a parameter
                            AssetDetailScreen(serial = serial, navController = navController, modifier= Modifier.padding(innerPadding))
                        }

                        // Define other composable routes here
                    }
                }
            }
        }
    }

}

