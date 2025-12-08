package com.example.finalproject.screens
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalproject.data.Category
import com.example.finalproject.data.TransactionType
import com.example.finalproject.navigation.Screen
import com.example.finalproject.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    navController: NavController,
    financeViewModel: FinanceViewModel
) {
    val categories by financeViewModel.categories.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Category?>(null) }
    var showEditDialog by remember { mutableStateOf<Category?>(null) }
    var showCannotDeleteDialog by remember { mutableStateOf<Category?>(null) }
    var categoryName by remember { mutableStateOf("") }
    var categoryType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var categoryColor by remember { mutableStateOf("#6200EE") }
    val colorOptions = listOf(
        "#6200EE", // Purple
        "#4ECDC4", // Teal
        "#45B7D1", // Blue
        "#FFA07A", // Light Salmon
        "#98D8C8", // Mint
        "#BB8FCE", // Light Purple
        "#85C1E2", // Sky Blue

    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Categories", fontSize = 24.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8FABE7),
                    titleContentColor = Color(0xFF000000)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.HomeScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        categoryName = ""
                        categoryType = TransactionType.EXPENSE
                        categoryColor = "#6200EE"
                        showAddDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Category",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (categories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No categories yet.\nClick + to add one!",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onItemClick = {
                                categoryName = category.name
                                categoryType = category.type
                                categoryColor = category.color
                                showEditDialog = category
                            },
                            onDeleteClick = {
                                // If it's in use, show the 'cannot delete' dialog

                                showDeleteDialog = category
                            }
                        )
                    }
                }
            }
        }
    }
    // "Cannot Delete" Error Dialog
    showCannotDeleteDialog?.let { categoryInUse ->
        AlertDialog(
            onDismissRequest = { showCannotDeleteDialog = null },
            title = { Text("Cannot Delete Category", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "You cannot delete the category '${categoryInUse.name}' " +
                            "because it is currently assigned to one or more transactions."
                )
            },
            confirmButton = {
                Button(onClick = { showCannotDeleteDialog = null }) {
                    Text("OK")
                }
            }
        )
    }

    // Add Category Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Category", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Type", fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clickable { categoryType = TransactionType.EXPENSE },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (categoryType == TransactionType.EXPENSE) 
                                    Color(0xFFE7DCF6) else Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Expense", fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clickable { categoryType = TransactionType.INCOME },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (categoryType == TransactionType.INCOME) 
                                    Color(0xFFE7DCF6) else Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Income", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Color", fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colorOptions.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = hexToColor(colorHex),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = if (categoryColor == colorHex) 3.dp else 0.dp,
                                        color = Color.Black,
                                        shape = CircleShape
                                    )
                                    .clickable { categoryColor = colorHex }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (categoryName.isNotBlank()) {
                            val newCategory = Category(
                                name = categoryName,
                                type = categoryType,
                                color = categoryColor
                            )
                            financeViewModel.addCategory(newCategory)
                            showAddDialog = false
                            categoryName = ""
                            categoryType = TransactionType.EXPENSE
                            categoryColor = "#6200EE"
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { 
                        showAddDialog = false
                        categoryName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Category Dialog
    showEditDialog?.let { categoryToEdit ->
        // Populate fields when dialog opens
        LaunchedEffect(categoryToEdit.id) {
            categoryName = categoryToEdit.name
            categoryType = categoryToEdit.type
            categoryColor = categoryToEdit.color
        }
        
        AlertDialog(
            onDismissRequest = { showEditDialog = null },
            title = { Text("Edit Category", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Type", fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clickable { categoryType = TransactionType.EXPENSE },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (categoryType == TransactionType.EXPENSE) 
                                    Color(0xFFE7DCF6) else Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Expense", fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clickable { categoryType = TransactionType.INCOME },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (categoryType == TransactionType.INCOME) 
                                    Color(0xFFE7DCF6) else Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Income", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Color", fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colorOptions.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = hexToColor(colorHex),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = if (categoryColor == colorHex) 3.dp else 0.dp,
                                        color = Color.Black,
                                        shape = CircleShape
                                    )
                                    .clickable { categoryColor = colorHex }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (categoryName.isNotBlank()) {
                            val updatedCategory = categoryToEdit.copy(
                                name = categoryName,
                                type = categoryType,
                                color = categoryColor
                            )
                            financeViewModel.updateCategory(updatedCategory)
                            showEditDialog = null
                            categoryName = ""
                            categoryType = TransactionType.EXPENSE
                            categoryColor = "#6200EE"
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = { 
                        showEditDialog = null
                        categoryName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { categoryToDelete ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Category", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete '${categoryToDelete.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        // --- THIS IS THE NEW LOGIC ---
                        // 1. Check if the category is in use WHEN the user confirms.
                        if (financeViewModel.isCategoryInUse(categoryToDelete.id)) {
                            // 2. If it is in use, hide this dialog and show the error dialog.
                            showDeleteDialog = null
                            showCannotDeleteDialog = categoryToDelete
                        } else {
                            // 3. If it's not in use, delete it and close the dialog.
                            financeViewModel.deleteCategory(categoryToDelete)
                            showDeleteDialog = null
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp) // Set the size of the circle
                    .background(
                        color = hexToColor(category.color), // Use our helper function here
                        shape = CircleShape,

                    )

            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.type.name,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Category",
                    tint = Color.Red
                )
            }
        }
    }
}
// Add this above the helper functions
// Add these functions at the bottom of your file

fun hexToColor(hex: String): Color {
    return try {
        // Parses a hex string like "#FF6B6B" into a Color object
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: IllegalArgumentException) {
        // If the color string is invalid, return a default gray color
        Color.Gray
    }
}

fun Color.toHexString(): String {
    // Converts a Color object back into a hex string for saving
    return String.format("#%06X", (0xFFFFFF and this.toArgb()))
}


