package com.example.assignment8

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
//import androidx.preference.isNotEmpty
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Opt-in to use experimental Material 3 API features, like the new Scaffold.

@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    // Collect the list of notes from the ViewModel as a State, so UI recomposes on changes.
    // --- ADD THIS LINE INSTEAD ---
    val contacts by viewModel.allContacts.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()
    // State for the title input field.
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var editingContact by remember { mutableStateOf<Contact?>(null) }
    var isNameError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }
    // State to control the visibility of the delete confirmation dialog.
    var showDeleteDialog by remember { mutableStateOf<Contact?>(null) }
    // Formatter for displaying dates in a consistent format.
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    // State for managing SnackBar messages.
    val snackbarHostState = remember { SnackbarHostState() }
    // Coroutine scope for launching suspending functions, like showing snackbars.
    val scope = rememberCoroutineScope()

    // Scaffold provides a basic screen layout with a SnackBarHost.
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        // Main column for arranging input fields, buttons, and the note list.
        // ... inside Scaffold { padding -> ... }

// Main column for arranging input fields, buttons, and the contact list.
        Column(
            modifier = Modifier
                .fillMaxSize() // Use fillMaxSize to take up the whole screen
                .padding(padding) // Apply padding from the Scaffold
                .padding(16.dp)   // Apply your own padding
        ) {
            // --- 1. INPUT SECTION ---
            Text("Add a Contact", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = name,
                onValueChange = {
                    name = it
                    isNameError = false // Clear error when user starts typing
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = isNameError, // Use the error state
                supportingText = { // Show error message if isNameError is true
                    if (isNameError) {
                        Text(
                            text = "Name cannot be empty",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = phone,
                onValueChange = {
                    phone = it
                    isPhoneError = false
                },

                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = isPhoneError, // Use the error state
                supportingText = {
                    if (isPhoneError) {
                        // Determine which error message to show based on the actual reason
                        val errorMessage = if (phone.isBlank()) {
                            "Phone cannot be empty"
                        } else {
                            "Invalid phone number format"
                        }
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                })

            Spacer(modifier = Modifier.height(8.dp))

            // Row for the Add/Update and Cancel buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Inside the Button's onClick lambda...
                Button(
                    onClick = {
                        // Check for errors first
                        isNameError = name.isBlank()
                        isPhoneError =  phone.isBlank() || !viewModel.isValidPhoneNumber(phone)

                        // Only proceed if there are no errors
                        if (!isNameError && !isPhoneError) {
                            val contactToSave = if (editingContact == null) {
                                Contact(name = name, phone = phone)
                            } else {
                                editingContact!!.copy(name = name, phone = phone)
                            }
                            viewModel.insert(contactToSave)

                            val message = if (editingContact == null) "Contact added!" else "Contact updated!"
                            scope.launch { snackbarHostState.showSnackbar(message) }

                            // Reset state
                            editingContact = null
                            name = ""
                            phone = ""
                        }
                }
                ) {
                    Text(if (editingContact == null) "Add" else "Update")
                /* ... */ }

                Button(onClick = {
                    // This calls the function in your ViewModel
                    viewModel.onSortOrderChange(SortOrder.ASC)
                }) {
                    Text("sort ASC")
                }

                Button(onClick = {
                    // This calls the function in your ViewModel
                    viewModel.onSortOrderChange(SortOrder.DESC)
                }) {
                    Text("sort DSC")
                }



                // Show "Cancel Edit" button only when a contact is being edited.
                if (editingContact != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = {
                        editingContact = null
                        name = ""
                        phone = ""
                    }) {
                        Text("Cancel Edit")
                    }
                }
            }
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) }, // This calls the VM function
                label = { Text("Search by Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- 2. SPACER ---
            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. LIST SECTION (This is where the LazyColumn goes) ---
            Text("Your Contacts", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn to efficiently display a scrollable list of contacts.
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(contacts.size) { idx -> // Use 'contacts'
                    val contact = contacts[idx] // Use 'contact'
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(contact.name, style = MaterialTheme.typography.titleSmall) // Use 'name'
                                Text(contact.phone, style = MaterialTheme.typography.bodyMedium) // Use 'phone'
                            }
                            Column {
                                Button(onClick = {
                                    editingContact = contact
                                    name = contact.name
                                    phone = contact.phone
                                }) {
                                    Text("Edit")
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(
                                    onClick = { showDeleteDialog = contact },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        } // <-- End of the main Column

// ... (The AlertDialog code remains here, outside the main Column)

        // Delete confirmation AlertDialog.
        // Delete confirmation AlertDialog.
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                // FIX: Update dialog text
                title = { Text("Delete Contact") },
                text = { Text("Are you sure you want to delete this contact?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Use a local variable to be safe
                            val contactToDelete = showDeleteDialog!!

                            // FIX 1: Call the correct delete function
                            viewModel.delete(contactToDelete)

                            // FIX 2: Update the snackbar message
                            scope.launch { snackbarHostState.showSnackbar("Contact deleted!") }
                            showDeleteDialog = null

                            // If the deleted contact was being edited, clear the input fields.
                            // FIX 3 & 4: Check and reset editingContact
                            if (editingContact == contactToDelete) {
                                editingContact = null
                                // FIX 5: Clear name and phone fields
                                name = ""
                                phone = ""
                            }
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
}
