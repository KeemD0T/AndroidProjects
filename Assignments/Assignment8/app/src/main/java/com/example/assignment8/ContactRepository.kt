package com.example.assignment8

import kotlinx.coroutines.flow.Flow

// 1. The repository takes the DAO as a constructor parameter.
// This is called "dependency injection".
class ContactRepository(private val contactDao: ContactDao) {

    // 2. Create a function for each function in your ContactDao.
    // These functions simply call the DAO's methods.

    fun getContactsSortedByNameAsc(): Flow<List<Contact>> {
        return contactDao.getContactsSortedByNameAsc()
    }

    fun getContactsSortedByNameDesc(): Flow<List<Contact>> {
        return contactDao.getContactsSortedByNameDesc()
    }

    fun findContacts(searchQuery: String): Flow<List<Contact>> {
        return contactDao.findContacts(searchQuery)
    }

    // You also need methods for inserting and deleting contacts
    // These should be 'suspend' functions because the DAO methods are.
    suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }
}
