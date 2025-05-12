package com.example.expensetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.expensetracker.data.database.DateConverter
import com.example.expensetracker.data.database.ExpenseTypeConverter
import java.util.Date
import java.util.UUID

enum class ExpenseType {
    YEMEK,
    ULASIM,
    ALISVERIS,
    EGLENCE,
    FATURA,
    DIGER
}

@Entity(tableName = "expenses")
@TypeConverters(DateConverter::class, ExpenseTypeConverter::class)
data class Expense(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val description: String,
    val type: ExpenseType,
    val date: Date = Date(),
    val people: Int = 1
) 