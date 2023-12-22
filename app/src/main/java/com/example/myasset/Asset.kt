package com.example.myasset

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Assets(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serial: String,
    val name: String,
    val category: String,
    val status: String,
    val holder: String,
    val location: String,
    val imageUri: String?
)
