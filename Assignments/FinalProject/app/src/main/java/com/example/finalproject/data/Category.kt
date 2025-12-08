package com.example.finalproject.data

import  androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val color: String = "#6200EE", // Default Material color
    val budgetLimit: Double? = null // Optional budget limit
)

