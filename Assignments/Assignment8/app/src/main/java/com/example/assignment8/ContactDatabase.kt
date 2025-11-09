package com.example.assignment8

//import androidx.privacysandbox.tools.core.generator.build
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// FIX 1: The class is now top-level and correctly named "ContactDatabase".
// FIX 2: The entity has been corrected to "Contact::class".
@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {

    // FIX 3: The abstract function now returns the correct DAO, "ContactDao".
    abstract fun contactDao(): ContactDao

    // Companion object for the singleton pattern (this part was mostly correct).
    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null // FIX 4: The INSTANCE type is now "ContactDatabase".

        fun getDatabase(context: Context): ContactDatabase {
            // This synchronized block ensures only one instance of the database is ever created.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java, // FIX 5: Use the correct database class here.
                    "contact_db" // FIX 6: Renamed the database file to be more descriptive.
                ).build()
                INSTANCE = instance
                // Return the newly created instance.
                instance
            }
        }
    }
}
