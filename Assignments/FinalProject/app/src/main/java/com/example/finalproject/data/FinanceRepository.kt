package com.example.finalproject.data
import kotlinx.coroutines.flow.Flow


class FinanceRepository(
    private val transactionDao: TransactionDao,
    private val CategoryDao: CategoryDao
){
    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)
    }

    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }

    fun getTransactionsByCategoryAndDateRange(categoryId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategoryAndDateRange(categoryId, startDate, endDate)
    }
    suspend fun getTotalByTypeAndDateRange(type: TransactionType, startDate: Long?, endDate: Long?): Double {
        return transactionDao.getTotalByTypeAndDateRange(type, startDate, endDate) ?: 0.0
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()
    val allCategories: Flow<List<Category>> = CategoryDao.getAllCategories()

    // Category management methods
    suspend fun insertCategory(category: Category) {
        CategoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        CategoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        CategoryDao.deleteCategory(category)
    }

    suspend fun getCategoryById(id: Long): Category? {
        return CategoryDao.getCategoryById(id)
    }

}





