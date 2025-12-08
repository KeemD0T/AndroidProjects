package com.example.finalproject.viewmodel

//import androidx.activity.result.launch
import android.icu.util.Calendar
import androidx.compose.foundation.layout.add
import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch // <-- ADD THIS
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.finalproject.data.FinanceRepository // This line is required
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.Transaction
import com.example.finalproject.data.Category // <-- ADD THIS LINE
import com.example.finalproject.data.TransactionType
import kotlinx.coroutines.flow.map


// Corrected: 'financeRepository' is now a private property of the class
class FinanceViewModel(private val financeRepository: FinanceRepository) : ViewModel() {


    // This is the private, mutable state that only the ViewModel can change.
    // It is initialized with a starting value, for example, 0.0.
    private val _currentBalance = MutableStateFlow(0.0)
    private val _Income = MutableStateFlow(0.0)
    private val _Expense = MutableStateFlow(0.0)

    //val allTransactions: LiveData<List<Transaction>> = financeRepository.allTransactions.asLiveData()

    // This is the public, read-only state that the UI will observe.
    // It is exposed as a StateFlow to prevent the UI from directly changing it.
    val currentbalance: StateFlow<Double> = _currentBalance.asStateFlow()
    val Income: StateFlow<Double> = _Income.asStateFlow()
    val Expense: StateFlow<Double> = _Expense.asStateFlow()

    val transactions: StateFlow<List<Transaction>> = financeRepository.allTransactions // <-- Correct Spelling
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val recentTransactions: StateFlow<List<Transaction>> = transactions.map { it.take(5) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<Category>> = financeRepository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    init {
        // Calculate initial totals when ViewModel is created
        viewModelScope.launch {
            updateDashboardTotals()
        }
        
        // Launch a coroutine that lives as long as the ViewModel
        viewModelScope.launch {
            // Collect from the original Flow (not the StateFlow) to get updates
            // This block will automatically re-run every time the
            // list of transactions in the database changes.
            financeRepository.allTransactions.collect { _ -> // We get the list but don't need to use it here
                // Call your existing function to recalculate and update the StateFlows
                updateDashboardTotals()
            }
        }
    }

    private suspend fun updateDashboardTotals() {
        // Fetch total income from the repository.
        // We pass null for dates to get all transactions.
        val totalIncome = financeRepository.getTotalByTypeAndDateRange(
            type = TransactionType.INCOME,
            startDate = Long.MIN_VALUE,
            endDate = Long.MAX_VALUE
        ) ?: 0.0

        // Fetch total expense from the repository.
        val totalExpense = financeRepository.getTotalByTypeAndDateRange(
            type = TransactionType.EXPENSE,
            startDate = Long.MIN_VALUE,
            endDate = Long.MAX_VALUE
        ) ?: 0.0

        // Update the StateFlows with the new values from the database.
        // Update on the main thread to ensure UI updates
        _Income.value = totalIncome
        _Expense.value = totalExpense
        _currentBalance.value = totalIncome - totalExpense
    }


//    private fun loadInitialBalance() {
//        // In a real app, this would be an async call to your data source.
//        // For this example, we'll just set a sample value.
//        _currentBalance.value = -100.0
//    }
//    private fun loadInitialIncome() {
//        _Income.value = 100.0
//    }
//    private fun loadInitialExpense() {
//        _Expense.value = 50.0
//
//
//    }


    fun updateBalance(newBalance: Double) {
        _currentBalance.value = newBalance
    }

    fun updateIncome(newIncome: Double) {
        _Income.value = newIncome
    }

    fun updateExpense(newExpense: Double) {
        _Expense.value = newExpense
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            financeRepository.updateTransaction(transaction)
        }
    }
    suspend fun getTransactionById(id: Long): Transaction? {
        return financeRepository.getTransactionById(id)
    }



    // A function the UI can call to add an expense, which updates the balance.
    fun addExpense(amount: Double) {
        // Use .update for safe, atomic updates to the state.
        _Expense.update { currentExpense -> currentExpense + amount }
        _currentBalance.update { current -> current - amount }
    }

    // A function the UI can call to add income, which updates the balance.
    fun addIncome(amount: Double) {
        _Income.update { currentIncome -> currentIncome + amount }
        _currentBalance.update { current -> current + amount }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            financeRepository.insertTransaction(transaction)

        }
    }
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            financeRepository.deleteTransaction(transaction)

        }
    }


    // Category management methods
    fun addCategory(category: Category) {
        viewModelScope.launch {
            financeRepository.insertCategory(category)
        }
    }
    fun CategoryName(id: Long): String {
        val category = categories.value.find { it.id == id }
        return category?.name ?: "Unknown Category"
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            financeRepository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            financeRepository.deleteCategory(category)
        }
    }
    fun isCategoryInUse(categoryId: Long): Boolean {
        // We check against the current value of the main transactions list.
        return transactions.value.any { it.categoryId == categoryId }
    }

    class FinanceViewModelFactory(
        private val repository: FinanceRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FinanceViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}