package com.example.assignment10

import kotlinx.coroutines.flow.Flow

// NoteRepository acts as an abstraction layer over the data source (NoteDao).
// It provides a clean API for the ViewModel to interact with data.
class NoteRepository(private val noteDao: NoteDao) {
    // Exposes a Flow of all notes, allowing for observing changes in the database.
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    // Suspended function to insert a new note into the database.
    // Marked as suspend because Room operations are asynchronous.
    suspend fun insert(note: Note) = noteDao.insert(note)

    // Suspended function to delete an existing note from the database.
    // Marked as suspend because Room operations are asynchronous.
    suspend fun delete(note: Note) = noteDao.delete(note)

    suspend fun update(note: Note) = noteDao.update(note)


}