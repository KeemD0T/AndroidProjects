package com.example.assignment10

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Note Data Access Object (DAO).
// This interface defines the methods for interacting with the 'notes' table in the database.
@Dao
interface NoteDao {
    // Query to retrieve all notes from the 'notes' table, ordered by date in descending order.
    // Returns a Flow, which emits updates whenever the data in the table changes.
    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAllNotes(): Flow<List<Note>>

    // Inserts a new note or replaces an existing one if there's a conflict (e.g., same primary key).
    // 'suspend' keyword indicates that this is a coroutine function and can be paused and resumed.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    // Deletes an existing note from the database.
    // 'suspend' keyword indicates that this is a coroutine function.
    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)
}
