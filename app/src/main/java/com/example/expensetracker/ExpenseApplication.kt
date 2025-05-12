package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.data.database.ExpenseDatabase
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ExpenseApplication : Application() {
    // Uygulama genelinde kullanılacak CoroutineScope
    private val applicationScope = CoroutineScope(SupervisorJob())
    
    // Veritabanı instance'ını tembel (lazy) olarak başlat
    private val database by lazy { ExpenseDatabase.getDatabase(this) }
    
    // Repository instance'ını tembel (lazy) olarak başlat
    val repository by lazy { ExpenseRepository(database.expenseDao()) }
} 