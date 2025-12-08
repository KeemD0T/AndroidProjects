package com.example.finalproject.screens


import android.app.ProgressDialog.show
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalproject.data.Transaction
import com.example.finalproject.viewmodel.FinanceViewModel
import com.example.finalproject.data.TransactionType
import com.example.finalproject.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
// 1. Add NavController to handle the "back" action after saving
fun AddEditTransactionScreen(
    navController: NavController,
    financeViewModel: FinanceViewModel,
    transactionId: Long
) {

    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    // --- State for the input fields ---
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) } // Default
    var isEditMode by remember { mutableStateOf(false) }
    // Get categories from ViewModel to find a valid categoryId
    val categories by financeViewModel.categories.collectAsState()
    var Categorytype by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf<com.example.finalproject.data.Category?>(null) }
    val filteredCategories = remember(transactionType, categories) {
        val filtered = categories.filter { it.type == transactionType }
        // If the current selection is no longer in the valid list, reset it.
        if (selectedCategory !in filtered) {
            selectedCategory = null
        }
        filtered
    }
    LaunchedEffect(key1 = transactionId, key2 = categories) {
        // Only run this logic if we are in "edit mode" (ID is not -1)
        if (transactionId != -1L) {
            // Call the suspend function to get the transaction data
            val transactionToEdit = financeViewModel.getTransactionById(transactionId)

            if (transactionToEdit != null) {
                isEditMode = true // Set edit mode state

                // ---- THIS FIXES THE ERRORS ----
                // Pre-fill the form fields using the fetched data
                amount = transactionToEdit.amount.toString()
                description = transactionToEdit.description
                transactionType = transactionToEdit.type

                // Find the matching category object from the full list
                // We also check that the 'categories' list is not empty before searching
                if (categories.isNotEmpty()) {
                    selectedCategory = categories.find { it.id == transactionToEdit.categoryId }
                }
            }
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                // We'll make the title dynamic later for editing
                title = {
                    Text(
                        text = if (isEditMode) "Edit Transaction" else "Add Transaction",
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8FABE7),
                    titleContentColor = Color(0xFF000000)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                      navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // 3. Build the form inside a Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp), // Add some spacing from the screen edges
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Type",
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f) // Takes up half the space
                        .height(80.dp) // Gives the card a nice height
                        .clickable {
                            transactionType = TransactionType.EXPENSE
                        }, // Navigation action
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE7DCF6)) // Light blue
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // Centers the text
                    ) {
                        Text(text = "Expense", fontWeight = FontWeight.SemiBold)
                        if (transactionType == TransactionType.EXPENSE) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.align(Alignment.TopEnd) // Position icon
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.size(16.dp))

                // --- Expense Card ---
                Card(
                    modifier = Modifier
                        .weight(1f) // Takes up the other half
                        .height(80.dp)
                        .clickable {
                            transactionType = TransactionType.INCOME
                        }, // Navigation action
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE7DCF6)) // Light blue
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // Centers the text
                    ) {
                        Text(text = "Income", fontWeight = FontWeight.SemiBold)
                        if (transactionType == TransactionType.INCOME) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.align(Alignment.TopEnd) // Position icon
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Category",
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            // --- Category Dropdown ---
            if (filteredCategories.isEmpty()) {
                Text(
                    text = "No categories available for this type. Go to the Categories screen to add one.",
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            } else {
                androidx.compose.foundation.lazy.LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredCategories.size) { index ->
                        val category = filteredCategories[index]
                        val isSelected = selectedCategory?.id == category.id

                        Card(
                            modifier = Modifier
                                .height(60.dp)
                                .clickable { selectedCategory = category },
                            shape = RoundedCornerShape(12.dp),
                            // Change color and border based on selection
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFDDEEFF) else Color(0xFFF0F0F0)
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color.Black) else null
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = category.name, fontWeight = FontWeight.SemiBold)
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp) // Give the checkmark some space
                                            .size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("amount") },
                modifier = Modifier.fillMaxWidth()
            )

            // TODO: Add a dropdown or radio buttons to select the transactionType
            if (showDatePicker) {
                // We need to get the parts of the date to pre-select them in the picker
                val calendar = java.util.Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                }
                val year = calendar.get(java.util.Calendar.YEAR)
                val month = calendar.get(java.util.Calendar.MONTH)
                val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

                android.app.DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        // This is called when the user selects a date
                        val newCalendar = java.util.Calendar.getInstance().apply {
                            set(selectedYear, selectedMonth, selectedDayOfMonth)
                        }
                        selectedDate = newCalendar.timeInMillis
                        showDatePicker = false // Hide the dialog
                    },
                    year,
                    month,
                    day
                ).apply {

                setOnCancelListener {
                    showDatePicker = false
                }
                show()
            }
                }
            Button(
                onClick = {
                    showDatePicker = true // Set the state to true to show the dialog
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Display the currently selected date, formatted nicely
                Text(text = "Date: ${formatDate(selectedDate)}")
            }

            LaunchedEffect(key1 = transactionId, key2 = categories) {
                if (transactionId != -1L) {
                    val transactionToEdit = financeViewModel.getTransactionById(transactionId)
                    if (transactionToEdit != null) {
                        isEditMode = true
                        amount = transactionToEdit.amount.toString()
                        description = transactionToEdit.description
                        transactionType = transactionToEdit.type
                        selectedDate = transactionToEdit.date // <-- ADD THIS LINE

                        if (categories.isNotEmpty()) {
                            selectedCategory = categories.find { it.id == transactionToEdit.categoryId }
                        }
                    }
                }
            }

            // 4. Save Button with the logic to add the transaction
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue != null && description.isNotBlank() && selectedCategory != null) {

                        if (isEditMode) {
                            // --- EDIT LOGIC ---
                            val updatedTransaction = Transaction(
                                id = transactionId, // CRITICAL: Use the original ID
                                amount = amountValue,
                                description = description,
                                type = transactionType,
                                date = System.currentTimeMillis(), // Or keep original date
                                categoryId = selectedCategory!!.id
                            )
                            // You will need to create this function in your ViewModel
                            financeViewModel.updateTransaction(updatedTransaction)
                        } else {
                            // --- ADD LOGIC ---
                            val newTransaction = Transaction(
                                amount = amountValue,
                                description = description,
                                type = transactionType,
                                date = System.currentTimeMillis(),
                                categoryId = selectedCategory!!.id
                            )
                            financeViewModel.addTransaction(newTransaction)
                        }

                        // Navigate back after saving
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Make the button text dynamic as well
                Text(if (isEditMode) "SAVE CHANGES" else "SAVE TRANSACTION")

            }
        }
    }
}
fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}