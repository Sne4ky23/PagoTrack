package com.pagotrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.pagotrack.app.data.AppDatabase
import com.pagotrack.app.data.ExpenseRepository
import com.pagotrack.app.ui.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val db = AppDatabase.getDatabase(this)
        val repository = ExpenseRepository(db.expenseDao())
        val viewModel = ExpenseViewModel(repository)
        
        setContent {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = Color(0xFF2196F3),
                    secondary = Color(0xFF03DAC6),
                    tertiary = Color(0xFFFF4081)
                )
            ) {
                Surface(color = Color(0xFFFAFAFA)) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}
