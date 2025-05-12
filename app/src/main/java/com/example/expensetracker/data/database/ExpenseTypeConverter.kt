package com.example.expensetracker.data.database

import androidx.room.TypeConverter
import com.example.expensetracker.model.ExpenseType

/**
 * Room için ExpenseType ve String tip dönüşümlerini sağlayan sınıf
 */
class ExpenseTypeConverter {
    @TypeConverter
    fun fromExpenseType(expenseType: ExpenseType): String {
        return expenseType.name
    }

    @TypeConverter
    fun toExpenseType(expenseTypeName: String): ExpenseType {
        return ExpenseType.valueOf(expenseTypeName)
    }
} 