package com.pagotrack.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    
    val confirmedExpenses: Flow<List<Expense>> = expenseDao.getConfirmedExpenses()
    
    val pendingExpenses: Flow<List<Expense>> = expenseDao.getPendingExpenses()
    
    val totalAmountConfirmed: Flow<Double> = expenseDao.getTotalAmountConfirmed()
        .map { it ?: 0.0 }
    
    fun getTodayExpenses(): Flow<List<Expense>> {
        return confirmedExpenses.map { expenses ->
            val today = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
            expenses.filter { expense ->
                expense.timestamp.startsWith(today)
            }
        }
    }
    
    fun getTodayTotal(): Flow<Double> {
        return getTodayExpenses().map { expenses ->
            expenses.sumOf { it.amount }
        }
    }
    
    suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insert(expense)
    }
    
    suspend fun updateExpense(expense: Expense) {
        expenseDao.update(expense)
    }
    
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.delete(expense)
    }
    
    suspend fun deleteAllExpenses() {
        expenseDao.deleteAll()
    }
    
    suspend fun confirmExpense(id: Long) {
        val expense = expenseDao.getExpenseById(id)
        expense.collect { exp ->
            expenseDao.update(exp.copy(confirmed = true))
        }
    }
}
