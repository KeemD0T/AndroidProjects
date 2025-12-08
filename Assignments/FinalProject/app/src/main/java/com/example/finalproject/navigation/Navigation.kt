package com.example.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.finalproject.data.FinanceDatabase
import com.example.finalproject.data.FinanceRepository
import com.example.finalproject.screens.AddEditTransactionScreen
import com.example.finalproject.screens.CategoryManagementScreen
import com.example.finalproject.screens.HomeScreen
import com.example.finalproject.screens.TransactionScreen
import com.example.finalproject.viewmodel.FinanceViewModel
// Add this line with your other imports



// 1. DEFINE your navigation routes here as a sealed class.
// This is the clean, type-safe way to handle navigation.
sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object TransactionScreen : Screen("transactions_screen")
    object AddEditTransactionScreen : Screen("add_edit_transaction_screen")
    object CategoryManagementScreen : Screen("category_management_screen")
}

/**
 * Defines the navigation graph for the application.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,

) {
    val context = LocalContext.current
    val db = FinanceDatabase.getDatabase(context)
    val repository = FinanceRepository(db.transactionDao(), db.categoryDao())
    val factory = FinanceViewModel.FinanceViewModelFactory(repository)
    val financeViewModel: FinanceViewModel = viewModel(factory = factory)


    // NavHost is the container for all of your app's destinations
    NavHost(
        navController = navController,
        // 2. USE your new Screen object for the start destination.
        startDestination = Screen.HomeScreen.route,
        modifier = modifier
    ) {
        // Define the "Home" screen destination
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                navController = navController,
                financeViewModel = financeViewModel
            )
        }

        // Define the "Transactions" screen destination
        composable(route = Screen.TransactionScreen.route) {
            TransactionScreen(
                navController = navController,
                financeViewModel = financeViewModel
            )
        }
        composable(
            // 1. Append the argument to the route string
            route = "${Screen.AddEditTransactionScreen.route}/{transactionId}",
            // 2. Define the argument, its type, and a default value
            arguments = listOf(navArgument("transactionId") {
                type = NavType.LongType
                defaultValue = -1L // Use -1 to signify "new transaction"
            })
        ) { backStackEntry ->
            // 3. Extract the ID from the backStackEntry arguments
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: -1L

            // 4. Pass the ID to your screen
            AddEditTransactionScreen(
                navController = navController,
                financeViewModel = financeViewModel,
                transactionId = transactionId
            )
        }

        composable(route = Screen.CategoryManagementScreen.route) {
            CategoryManagementScreen(
                navController = navController,
                financeViewModel = financeViewModel
            )
        }
    }
}
