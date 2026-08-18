package com.pagotrack.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double = 0.0,
    val currency: String = "EUR",
    val merchant: String = "",
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    val confirmed: Boolean = false
)
