package com.example.assignment8

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flatMapLatest

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.ASC)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val allContacts: Flow<List<Contact>> = combine(
        _searchQuery,
        _sortOrder
    ) { query, sortOrder ->
        when (sortOrder) {
            SortOrder.ASC -> {
                if (query.isBlank()) {
                    repository.getContactsSortedByNameAsc()
                } else {
                    repository.findContacts("%${query}%")
                }
            }
            SortOrder.DESC -> {
                if (query.isBlank()) {
                    repository.getContactsSortedByNameDesc()
                } else {
                    repository.findContacts("%${query}%")
                }
            }
        }
    }.flatMapLatest  { it }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }

    fun insert(contact: Contact) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch {
        repository.delete(contact)
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches("^[0-9]{3}\\.[0-9]{3}\\.[0-9]{4}$".toRegex())
    }

    companion object {
        fun provideFactory(
            application: Application,
        ): ViewModelProvider.Factory {
            return ContactViewModelFactory(ContactRepository(ContactDatabase.getDatabase(application).contactDao()))
        }
    }
}

class ContactViewModelFactory(private val repository: ContactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

enum class SortOrder {
    ASC,
    DESC
}
