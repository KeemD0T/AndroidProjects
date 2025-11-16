package com.example.assignment10

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates the class as a Room database, specifying the entities it contains (Note) and the version.
// exportSchema is set to false to prevent exporting schema into a folder.
@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    // Abstract method to provide the Data Access Object (DAO) for notes.
    // Room will generate the implementation for this method.
    abstract fun noteDao(): NoteDao

    // Companion object allows us to define static-like members and methods for the database.
    companion object {
        // The @Volatile annotation ensures that changes to the INSTANCE variable are immediately
        // visible to all threads. This is crucial for thread-safe singleton implementation.
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        // Provides a singleton instance of the NoteDatabase.
        // If INSTANCE is null, it creates the database in a synchronized block to prevent multiple instances.
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here if INSTANCE is null.
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Application context to prevent memory leaks.
                    NoteDatabase::class.java,
                    "notes_db" // The name of the database file.
                )
                    .fallbackToDestructiveMigration() // Allows Room to recreate the database when version changes
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }
}