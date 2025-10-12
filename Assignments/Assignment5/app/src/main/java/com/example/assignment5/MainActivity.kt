package com.example.assignment5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview


/**
 * This project covers concepts from Chapter 8 lessons:
 * - "Lazy Column" - for creating scrollable contact lists
 * - "Handling Clicks and Selection" - for interactive contact selection
 * - "Combining LazyColumn and LazyRow" - for understanding list composition
 *
 * Students should review these lessons before starting:
 * - LazyColumn lesson for list implementation
 * - Clicks and Selection lesson for interactive behavior
 * - Combined lesson for understanding how lists work together
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactListApp()
                    // TODO: Call the main composable function
                    // Add ContactListApp() here
                }
            }
        }
    }
}




@Composable
fun ContactListApp() {

    // TODO: Create sample contact data
    // Create a list of 25 contacts with names, emails, and phone numbers
    // Use the Contact data class provided below
    // See "Lazy Column" lesson for examples of data structures and LazyColumn usage
    val contacts = listOf(
        Contact("John Doe", "john@example.com", "555-0101"),
        Contact("Jane Smith", "jane@example.com", "555-0102"),
        Contact("Bob Johnson", "bob@example.com", "555-0103"),
        Contact("Alice Brown", "alice@example.com", "555-0104"),
        Contact("Charlie White", "charlie@example.com", "555-0105"),
        Contact("Dave Gray", "dave@example.com", "555-0106"),
        Contact("Eve Black", "eve@example.com", "555-0107"),
        Contact("Frank Green", "frank@example.com", "555-0108"),
        Contact("Grace Red", "grace@example.com", "555-0109"),
        Contact("Hank Yellow", "hank@example.com", "555-0110"),
        Contact("Ivy Purple", "ivy@example.com", "555-0111"),
        Contact("Jack Blue", "jack@example.com", "555-0112"),
        Contact("Kelly Orange", "kelly@example.com", "555-0113"),
        Contact("Leo Teal", "leo@example.com", "555-0114"),
        Contact("Mia Violet", "mia@example.com", "555-0115"),
        Contact("Nate Navy", "nate@example.com", "555-0116"),
        Contact("Olivia Olive", "olivia@example.com", "555-0117"),
        Contact("Peter Pink", "peter@example.com", "555-0118"),
        Contact("Quincy Turquoise", "quincy@example.com", "555-0119"),
        Contact("Rachel Brown", "rachel@example.com", "555-0120"),
        Contact("Sam Silver", "sam@example.com", "555-0121"),
        Contact("Tina Tan", "tina@example.com", "555-0122"),
        Contact("Ulysses United", "ulysses@example.com", "555-0123"),
        Contact("Vera Violet", "vera@example.com", "555-0124"),
        Contact("Wayne White", "wayne@example.com", "555-0125"),
        Contact("Xavier Yellow", "xavier@example.com", "555-0126"),

        // TODO: Add 25Contact objects here
        // Example: Contact("John Doe", "john@example.com", "555-0101")
    )
    ContactList(contacts)
    // TODO: Call the ContactList composable
    // Pass the contacts list as a parameter
}

@Composable
fun ContactList(contacts: List<Contact>) {
    var selectedContact by rememberSaveable { mutableStateOf<Contact?>(null) }

    Column(
       modifier = Modifier
           .fillMaxSize()
            .padding(16.dp)
             .padding(top = 50.dp)
    ) {
        Text(
           text = "Contact List",
           style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
          Text(
              text = "Selected: ${selectedContact?.name ?: "No contact selected"}",
              color = if (selectedContact != null) {
                  MaterialTheme.colorScheme.primary
              } else {
                  Color.Unspecified // Use the default color when nothing is selected
              },


              style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)

          )



        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(contacts) { contact ->
                ContactItem(
                    contact = contact,
                    isSelected = contact == selectedContact,
                    onClick = {
                        selectedContact = if (selectedContact == contact) {
                            null
                        } else {
                            contact
                        }
                    }
                )
            }
        }
        if (selectedContact != null) {
            Button(
                onClick = {
                    selectedContact = null // Clear the selection
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Clear Selection")
            }
        }
    }

            // TODO: Create state variable for selected contact
            // Use remember and mutableStateOf to track which contact is selected
            // Type should be Contact? (nullable Contact)
            // See "Handling Clicks and Selection" lesson for examples of state management and selection tracking

        // TODO: Add header text
        // Create a Text composable with:
        // - text = "Contact List"
        // - style = MaterialTheme.typography.headlineMedium
        // - modifier = Modifier.padding(bottom = 16.dp)

        // TODO: Add selection status text
        // Create a Text composable that shows:
        // - "Selected: [contact name]" if a contact is selected
        // - "No contact selected" if no contact is selected
        // - style = MaterialTheme.typography.bodyLarge
        // - modifier = Modifier.padding(bottom = 24.dp)

        // TODO: Create LazyColumn for the contact list
        // Use LazyColumn with:
        // - verticalArrangement = Arrangement.spacedBy(8.dp)
        // - items(contacts) to iterate through the contacts
        // - Call ContactItem for each contact with proper parameters
        // See "Lazy Column" lesson for complete LazyColumn implementation examples

        // TODO: Add clear selection button
        // Only show this button when a contact is selected
        // Use Button with:
        // - onClick to clear the selection
        // - modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        // - Text("Clear Selection")
        // See "Handling Clicks and Selection" lesson for examples of conditional UI and button interactions
    }




@Composable
fun ContactItem(
    contact: Contact,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        },
                        shape = CircleShape
                    ),

            ){
                Text(
                    text = "${contact.name.first()}${contact.name.substringAfterLast(" ").first()}",

                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column(
                // Modifier chain fixed with a dot '.'
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                // Text for the name
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    // Color changes when selected
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
                )

                Spacer(modifier = Modifier.height(4.dp)) // Adds a little space

                // Text for the email
                Text(
                    text = contact.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )

                // Text for the phone number
                Text(
                    text = contact.phone,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // This 'if' block replaces the selectionIndicator call
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // TODO: Create contact avatar (colored circle with initials)
            // Use Box with:
            // - modifier = Modifier.size(50.dp)
            // - background with conditional color (primary when selected, secondary when not)
            // - shape = CircleShape
            // - contentAlignment = Alignment.Center
            // - Text with contact initials (first letter of each name)
            // See "Handling Clicks and Selection" lesson for examples of conditional styling based on selection state

            Spacer(modifier = Modifier.width(16.dp))

            // TODO: Create contact information column
            // Use Column with:
            // - modifier = Modifier.weight(1f)
            // - Text for name (titleMedium, bold, conditional color)
            // - Text for email (bodyMedium, conditional color)
            // - Text for phone (bodySmall, conditional color)
            // See "Handling Clicks and Selection" lesson for examples of conditional text styling and layout

            // TODO: Add selection indicator
            // Only show when contact is selected
            // Use Icon with:
            // - imageVector = Icons.Default.Check
            // - contentDescription = "Selected"
            // - tint = MaterialTheme.colorScheme.primary
            // - modifier = Modifier.size(24.dp)
            // See "Handling Clicks and Selection" lesson for examples of conditional UI elements and visual feedback
        }
    }
}

// Data class for contact information
data class Contact(
    val name: String,
    val email: String,
    val phone: String
)

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactListAppPreview() {
    ContactListApp()
}
