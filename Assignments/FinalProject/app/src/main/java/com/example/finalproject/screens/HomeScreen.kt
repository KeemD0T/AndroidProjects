package com.example.finalproject.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavController
import com.example.finalproject.navigation.Screen
import com.example.finalproject.viewmodel.FinanceViewModel
//import com.example.finalproject.viewmodel.FinanceViewModelFactory
import com.example.finalproject.screens.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    financeViewModel: FinanceViewModel
) {
    // Use the shared ViewModel passed from Navigation
    val currentbalance by financeViewModel.currentbalance.collectAsState()
    val income by financeViewModel.Income.collectAsState()
    val expense by financeViewModel.Expense.collectAsState()
val recentTransactions by financeViewModel.recentTransactions.collectAsState()
val categories by financeViewModel.categories.collectAsState()



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Finance Tracker", fontSize = 24.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE7DAFC),
                    titleContentColor = Color(0xFF000000)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val balanceCardColor = if (currentbalance >= 0) {
                Color(0xFFE7DAFC) // Your original blue color for positive balance
            } else {
                Color(0xFFF6DBD9) // A dark, reddish color for negative balance
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = balanceCardColor), // <-- Use the dynamic variable
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            )
            {
                // This single Column will layout all the card's content
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    // This centers the Icon and Text horizontally
                    horizontalAlignment = Alignment.Start
                ) {

                        // 1. Display Icon based on balance
                    if (currentbalance >= 0) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Positive Balance",
                            tint = Color(0xFF000901), // Green color
                            modifier = Modifier.size(48.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Negative Balance",
                            tint = Color(0xFF111111), // Red color
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    // Add a small space between the icon and the text
                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. "Current Balance" label text (made smaller)
                    Text(
                        text = "Current Balance",
                        fontWeight = FontWeight.Normal, // Less emphasis
                        fontSize = 18.sp, // <<< FONT SIZE REDUCED
                        color = Color(0xFF0C0C0C)
                    )

                    // 3. The actual balance amount
                    Text(
                        // Format the balance to 2 decimal places for currency
                        text = "$${String.format("%.2f", currentbalance)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp, // Keep this large for emphasis
                        color = Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly // This spaces the cards out
            ) {
                // --- Income Card ---
                Card(
                    modifier = Modifier.weight(1f), // Each card takes up equal space
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3EBF3E)) // Light green background
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(text = "Income", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Text(
                            text = "$${String.format("%.2f", income)}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = Color(0xFF011102),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(16.dp)) // Space between the two cards

                // --- Expense Card ---
                Card(
                    modifier = Modifier.weight(1f), // Each card takes up equal space
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF17D75)) // Light red background
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = "Expense", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Text(
                            text = "$${String.format("%.2f", expense)}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = Color(0xFF150000),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))// Space between the two rows

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Use same padding as cards for alignment
                horizontalAlignment = Alignment.Start // Aligns content to the left
            ) {
                Text(
                    text = "Quick Actions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp, // Made slightly larger for a section title
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // --- Transactions Card Button ---
                    Card(
                        modifier = Modifier
                            .weight(1f) // Takes up half the space
                            .height(80.dp) // Gives the card a nice height
                            .clickable { navController.navigate(Screen.TransactionScreen.route) }, // Navigation action
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7DCF6)) // Light blue
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center // Centers the text
                        ) {
                            Text(text = "Transactions", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    // --- Categories Card Button ---
                    Card(
                        modifier = Modifier
                            .weight(1f) // Takes up the other half
                            .height(80.dp)
                            .clickable { navController.navigate(Screen.CategoryManagementScreen.route) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6DBF4)) // Light purple
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Categories", fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Use same padding as cards for alignment
                horizontalAlignment = Alignment.Start // Aligns content to the left
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ){

                    Text(
                        text = "Recent Transactions (Most Recent 5)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp, // Made slightly larger for a section title
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { navController.navigate(Screen.TransactionScreen.route) },

                    ) {
                        Text(text = "View All", fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Display the last 5 transactions
                if (recentTransactions.isEmpty()) {
                    Text(
                        text = "No transactions recorded yet.",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    // LazyColumn is used for building efficient, scrollable lists.
                    LazyColumn {
                        items(recentTransactions.size) { index ->
                            val transaction = recentTransactions[index]
                            val category = categories.find { it.id == transaction.categoryId }
                            val categoryName = category?.name ?: "Uncategorized"
                            TransactionItem(
                                transaction = transaction,
                                categoryName = categoryName,
                                // Since the delete button is hidden, this click action is not strictly needed
                                // but we can leave it as is.
                                onDeleteClick = { },
                                onItemClick = {
                                    // Clicking a recent item should take you to the full list or edit screen
                                    navController.navigate("${Screen.AddEditTransactionScreen.route}/${transaction.id}")
                                },
                                showDeleteButton = false // <-- ADD THIS LINE
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                             // Adds space between items
                        }
                    }
                }
            }
        }
    }






 //A preview function helps you see your UI in the Android Studio editor
