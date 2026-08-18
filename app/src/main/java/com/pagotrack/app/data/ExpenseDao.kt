package com.pagotrack.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    
    @Insert
    suspend fun insert(expense: Expense): Long
    
    @Update
    suspend fun update(expense: Expense)
    
    @Delete
    suspend fun delete(expense: Expense)
    
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<Expense>>
    
    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Long): Flow<Expense>
    
    @Query("SELECT * FROM expenses WHERE confirmed = 1 ORDER BY timestamp DESC")
    fun getConfirmedExpenses(): Flow<List<Expense>>
    
    @Query("SELECT * FROM expenses WHERE confirmed = 0 ORDER BY timestamp DESC")
    fun getPendingExpenses(): Flow<List<Expense>>
    
    @Query("DELETE FROM expenses")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM expenses WHERE confirmed = 1")
    fun getConfirmedCount(): Flow<Int>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE confirmed = 1")
    fun getTotalAmountConfirmed(): Flow<Double?>
}
