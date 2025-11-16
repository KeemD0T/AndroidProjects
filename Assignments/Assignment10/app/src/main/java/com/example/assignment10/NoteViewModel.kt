package com.example.assignment10

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// NoteViewModel acts as a communication bridge between the UI (NoteScreen) and the data repository.
// It exposes data to the UI and handles UI-related data operations, abstracting the data source.
class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    // Expose a StateFlow of notes from the repository. This allows the UI to observe changes.
    // stateIn converts a Flow into a StateFlow, ensuring it's always active while subscribed.
    val notes: StateFlow<List<Note>> = repository.allNotes.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), // Start collecting when there's an active subscriber and stop 5s after the last subscriber disappears.
        emptyList()
    )

    // Function to add a new note. It launches a coroutine in the viewModelScope
    // to perform the insert operation asynchronously via the repository.
    fun addNote(title: String, content: String, date: String) {
        viewModelScope.launch {
            repository.insert(Note(title = title, content = content, date = date))
        }
    }

    // Function to delete an existing note. It launches a coroutine in the viewModelScope
    // to perform the delete operation asynchronously via the repository.
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun toggleFavorite(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedNote = note.copy(isFavorited = !note.isFavorited)
            repository.update(updatedNote)
        }
    }

    // Function to update an existing note. It launches a coroutine in the viewModelScope
    // to perform the update operation asynchronously via the repository.
    fun updateNote(note: Note, title: String, content: String, date: String) {
        viewModelScope.launch {
            val updatedNote = note.copy(title = title, content = content, date = date)
            repository.update(updatedNote)
        }
    }

    // Companion object to provide a factory for the NoteViewModel.
    // This is necessary because NoteViewModel has a constructor that takes NoteRepository.
    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            // Create a NoteRepository, which in turn depends on NoteDao from NoteDatabase.
            return NoteViewModelFactory(NoteRepository(NoteDatabase.getDatabase(application).noteDao
                ()))
        }
    }
}

// NoteViewModelFactory is a custom ViewModelProvider.Factory that allows us to instantiate
// NoteViewModel with a NoteRepository dependency.
class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    // The create method is responsible for creating new ViewModel instances.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is NoteViewModel.
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            // If it is, create and return a new NoteViewModel instance.
            @Suppress("UNCHECKED_CAST") // Suppress the unchecked cast warning as we've checked the type.
            return NoteViewModel(repository) as T
        }
        // If an unknown ViewModel class is requested, throw an exception.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}