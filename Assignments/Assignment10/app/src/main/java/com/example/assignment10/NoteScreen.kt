package com.example.assignment10


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring

import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer




import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Opt-in to use experimental Material 3 API features, like the new Scaffold.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(viewModel: NoteViewModel) {
    // Collect the list of notes from the ViewModel as a State, so UI recomposes on changes.
    val notes by viewModel.notes.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var titleTouched by remember { mutableStateOf(false) }
    var contentTouched by remember { mutableStateOf(false) }
    // State to hold the note currently being edited, if any.
    var editingNote by remember { mutableStateOf<Note?>(null) }
    // State to control the visibility of the delete confirmation dialog.
    var showDeleteDialog by remember { mutableStateOf<Note?>(null) }
    // Formatter for displaying dates in a consistent format.
    var expandedNoteId by remember { mutableStateOf<Int?>(null) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    // State for managing SnackBar messages.
    val snackbarHostState = remember { SnackbarHostState() }
    // Coroutine scope for launching suspending functions, like showing snackbars.
    val scope = rememberCoroutineScope()

    val isTitleError = titleTouched && title.isBlank()
    val isContentError = contentTouched && content.isBlank()

    val customColors = lightColorScheme(
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC6)
    )
    // Scaffold provides a basic screen layout with a SnackBarHost.
    MaterialTheme(
        colorScheme = customColors
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Material Notes",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {

                    FloatingActionButton(
                        onClick = {
                            title = ""
                            content = ""
                            editingNote = null

                            titleTouched = false
                            contentTouched = false
                        },
                        containerColor = Color(0xFF00C853),
                        contentColor = Color.Black,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            // --- THIS IS THE MAIN LAYOUT COLUMN. EVERYTHING GOES INSIDE IT. ---
            Column(
                modifier = Modifier
                    .padding(padding) // Use padding values from Scaffold
                    .fillMaxSize()
                    .padding(16.dp) // Add overall padding for the screen
            ) {
                // --- NOTE INPUT CARD ---
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = if (editingNote == null) "Create a New Note" else "Edit Note",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it
                                              titleTouched = true
                            },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = isTitleError,
                            supportingText = {
                                if (isTitleError) {
                                    Text("Title cannot be empty")
                                }
                            }

                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it
                                              contentTouched = true
                            },
                            label = { Text("Content") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = isContentError,
                            supportingText = {
                                if (isContentError) {
                                    Text("Content cannot be empty")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        // --- ACTION BUTTONS ROW (INSIDE THE CARD's COLUMN) ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.Left,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (editingNote != null) {
                                OutlinedButton(onClick = {
                                    editingNote = null
                                    title = ""
                                    content = ""
                                }) {
                                    Text("Cancel Edit")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            // This is the main Add/Update button, now in the correct place.
                            Button(
                                onClick = {
                                    if (editingNote == null) {
                                        viewModel.addNote(title, content, dateFormat.format(Date()))
                                        scope.launch { snackbarHostState.showSnackbar("Note added!") }
                                    } else {
                                        viewModel.updateNote(editingNote!!, title, content, dateFormat.format(Date()))
                                        editingNote = null
                                        scope.launch { snackbarHostState.showSnackbar("Note updated!") }
                                    }
                                    title = ""
                                    content = ""

                                    titleTouched = false
                                    contentTouched = false

                                },
                                enabled = title.isNotBlank() && content.isNotBlank()
                            ) {
                                Text(if (editingNote == null) "Add Note" else "Update Note")
                            }
                        }
                    }
                } // End of Input Card

                // The elements below are now CORRECTLY inside the main Column.
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Notes", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                // --- NOTES LIST ---
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Adds space between cards


                ) {
                    items(notes) { note ->

                        val cardColor by animateColorAsState(
                            targetValue = if (note.isFavorited) Color(0xFFCD4AD9) else MaterialTheme.colorScheme.surfaceVariant,
                            animationSpec = tween(durationMillis = 300),
                        )
                        // --- PASTE THIS CODE WHERE YOUR CURSOR WAS ---


                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()

                        var isClicked by remember { mutableStateOf(false) }

                        

                        // Click animation: bounce effect

                        val clickScale by animateFloatAsState(

                            targetValue = if (isClicked) 0.92f else 1f,

                            animationSpec = spring(

                                dampingRatio = Spring.DampingRatioMediumBouncy,

                                stiffness = Spring.StiffnessHigh

                            ),

                            label = "ClickAnimation",

                            finishedListener = {

                                if (isClicked) {

                                    isClicked = false

                                }

                            }

                        )

                        val scale by animateFloatAsState(
                            targetValue = if (isPressed) 0.97f else clickScale, // Combine press and click animations
                            animationSpec = spring( // Use a spring for a natural bounce
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            label = "CardScaleAnimation"
                        )



                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = {
                                        isClicked = true
                                        editingNote = note
                                        title = note.title
                                        content = note.content
                                    }
                                ),

                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    colors = CardDefaults.cardColors(
                                    containerColor = cardColor
                                    )

                        ) {
                            Column(modifier = Modifier
                                .padding(16.dp)

                            ) {
                                Text(text = note.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)


                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = note.content)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Last updated: ${note.date}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(onClick = {
                                        viewModel.toggleFavorite(note)
                                    }) {
                                        Icon(
                                            Icons.Filled.Star,
                                            contentDescription = "Favorite Note",
                                            tint = if (note.isFavorited) Color(0xFF00BCD4) else Color.Gray
                                        )
                                    }
                                    IconButton(onClick = { showDeleteDialog = note }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
                                    }
                                }
                            }
                        }
                    }
                } // End of LazyColumn
            } // --- END OF MAIN LAYOUT COLUMN ---

            // --- DELETE DIALOG ---
            // This stays at the end, as it's an overlay.
            if (showDeleteDialog != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = null },
                    title = { Text("Delete Note") },
                    text = { Text("Are you sure you want to delete this note?") },
                    confirmButton = {
                        TextButton(onClick = {
                            val noteToDelete = showDeleteDialog!!
                            viewModel.deleteNote(noteToDelete)
                            scope.launch { snackbarHostState.showSnackbar("Note deleted!") }
                            if (editingNote == noteToDelete) {
                                editingNote = null
                                title = ""
                                content = ""
                            }
                            showDeleteDialog = null
                        }) {
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
        } // End of Scaffold
    } // End of MaterialTheme
}

