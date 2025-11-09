package com.example.assignment8



import androidx.room.Entity
import androidx.room.PrimaryKey

// Add these two annotations
@Entity(tableName = "contact_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // The primary key
    val name: String,
    val phone: String
)
