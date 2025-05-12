package com.example.expensetracker.data.database

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room için Date ve Long tip dönüşümlerini sağlayan sınıf
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
} 