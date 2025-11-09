package com.example.assignment8

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert
    suspend fun insert(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM contact_table WHERE name LIKE :searchQuery")
    fun findContacts(searchQuery: String): Flow<List<Contact>>

    @Query("SELECT * FROM contact_table ORDER BY name DESC")
    fun getContactsSortedByNameDesc(): Flow<List<Contact>>

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    fun getContactsSortedByNameAsc(): Flow<List<Contact>>
}
