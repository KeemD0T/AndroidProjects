package com.example.finalproject.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.data.Transaction
import com.example.finalproject.data.TransactionType
import com.example.finalproject.viewmodel.FinanceViewModel
import com.example.finalproject.navigation.Screen
//import androidx.compose.material.icons.filled
import androidx.compose.material.icons.filled.Add
import androidx.navigation.NavController
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
// 1. Fixed the typo: "finaceViewModel" -> "financeViewModel"
fun TransactionScreen(
    navController: NavController,
    financeViewModel: FinanceViewModel,
) {
    // State for the input fields
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf<Long?>(null) } // null means "All"


    // Use the shared ViewModel passed from Navigation instead of creating a new one
    val transactions by financeViewModel.transactions.collectAsState()
    val categories by financeViewModel.categories.collectAsState()
    val filteredTransactions = remember(transactions, selectedCategoryFilter) {
        if (selectedCategoryFilter == null) {
            transactions // If no filter is selected, show all transactions
        } else {
            transactions.filter { it.categoryId == selectedCategoryFilter }
        }
    }
    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text(text = "Transactions", fontSize = 24.sp) }, // Corrected title
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8FABE7),
                    titleContentColor = Color(0xFF000000)
                ),
                // --- 2. FIX: ADD THIS 'actions' BLOCK ---
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.HomeScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = Color.Black // Match the other icons
                        )
                    }
                },

                actions = {
                    TextButton(onClick = {
                        // TODO: Implement filter logic here
                        showFilterDialog = true
                    }) {
                        Text(
                            text = "Filter",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    IconButton(onClick = {
                        // Navigate to add new transaction (use -1 for new transaction)
                        navController.navigate("${Screen.AddEditTransactionScreen.route}/-1")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Transaction",
                            tint = Color.Black // Match the title color
                        )
                    }

                }
            )
        }
    )
        // 3. FIX: REMOVED the floatingActionButton from here
     { innerPadding ->
         LazyColumn(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(innerPadding), // Use the padding provided by Scaffold
             verticalArrangement = Arrangement.spacedBy(8.dp),

             ) {
             // This is the ONLY block you need for displaying transactions.
             if (filteredTransactions.isEmpty()) {
                 item {
                     Text(
                         text = "No transactions found. Add one to get started!",
                         modifier = Modifier.padding(16.dp),
                         style = MaterialTheme.typography.bodyLarge
                     )
                 }
             } else {
                 items(filteredTransactions, key = { it.id }) { transaction ->
                     val category = categories.find { it.id == transaction.categoryId }
                     val categoryName = category?.name ?: "Uncategorized"

                     TransactionItem(
                         transaction = transaction,
                         categoryName = categoryName,
                         onDeleteClick = {
                             transactionToDelete = transaction
                         },
                         onItemClick = {
                             navController.navigate("${Screen.AddEditTransactionScreen.route}/${transaction.id}")
                         },
                         showDeleteButton = true
                     )
                 }
             }
         }

         transactionToDelete?.let { transaction ->
            AlertDialog(
                onDismissRequest = { transactionToDelete = null }, // Close dialog on back press or click outside
                title = { Text("Delete Transaction", fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to delete this transaction?") },
                confirmButton = {
                    Button(
                        onClick = {
                            financeViewModel.deleteTransaction(transaction)
                            transactionToDelete = null // Close the dialog after deleting
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { transactionToDelete = null } // Close dialog on "Cancel"
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    } // End of Scaffold
// In TransactionScreen.kt, just before the end of the composable function

// ... (after the delete confirmation dialog)

    // --- ADD THE FILTER DIALOG ---
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter by Category", fontWeight = FontWeight.Bold) },
            text = {
                // Use a LazyColumn inside the dialog for scrollable categories
                LazyColumn {
                    // "Show All" option
                    item {
                        Text(
                            text = "Show All",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategoryFilter = null // Set filter to null
                                    showFilterDialog = false
                                }
                                .padding(vertical = 12.dp),
                            fontWeight = if (selectedCategoryFilter == null) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    // List of all categories
                    items(categories) { category ->
                        Text(
                            text = category.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategoryFilter = category.id // Set filter to the selected category ID
                                    showFilterDialog = false
                                }
                                .padding(vertical = 12.dp),
                            fontWeight = if (selectedCategoryFilter == category.id) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

} // End of TransactionScreen composable










@Composable
fun TransactionItem(
    transaction: Transaction,
    onDeleteClick: () -> Unit,
    categoryName: String,
    onItemClick: () -> Unit,
    showDeleteButton: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick), // The whole card is clickable
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // This is key to align items nicely
        ) {
            // --- Left Side: Description ---
            Column(modifier = Modifier.weight(1f)) { // Use a column for future flexibility
               Text(
                   text = transaction.description,
                   fontWeight = FontWeight.Bold,
                   fontSize = 16.sp
               )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 1. Date Text
                    Text(
                        text = formatDate(transaction.date),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    // 2. A separator between them
                    Spacer(modifier = Modifier.size(8.dp)) // Adds a small horizontal space
                    Text(
                        text = "•",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(8.dp)) // Adds another small horizontal space

                    // 3. Category Name Text
                    Text(
                        text = categoryName,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

                // --- Right Side: Amount and Delete Icon ---
                // The amount text will be aligned to the start of this Row section
                Text(
                    // Format the text to show 2 decimal places and a sign
                    text = if (transaction.type == TransactionType.INCOME) "+$${
                        String.format(
                            "%.2f",
                            transaction.amount
                        )
                    }" else "-$${String.format("%.2f", transaction.amount)}",
                    color = if (transaction.type == TransactionType.INCOME) Color(0xFF006400) else Color.Red, // Dark Green for income, Red for expense
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 16.dp) // Space between amount and icon
                )

                // Delete Icon
                if (showDeleteButton) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(24.dp) // Control the size of the touch target
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Transaction",
                            tint = Color.Red// A more subtle color for the icon
                        )

                    }
                }
            }
        }
    }

