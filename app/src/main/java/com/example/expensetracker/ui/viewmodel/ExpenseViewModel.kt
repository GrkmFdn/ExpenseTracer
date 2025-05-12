package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.repository.ExpenseRepository
import com.example.expensetracker.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {
    
    // Tüm harcamalar
    val allExpenses: StateFlow<List<Expense>> = repository.allExpenses
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    // Bu haftanın harcamaları
    val currentWeekExpenses: StateFlow<List<Expense>> = repository.getCurrentWeekExpenses()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    // Bu haftanın toplam harcama miktarı
    val currentWeekTotalExpense: StateFlow<Double> = repository.getCurrentWeekTotalExpense()
        .map { it ?: 0.0 }
        .catch { emit(0.0) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    // Tarihe göre gruplanmış harcamalar
    val groupedExpenses: StateFlow<Map<String, List<Expense>>> = allExpenses
        .combine(MutableStateFlow(0)) { expenses, _ ->
            expenses.groupBy { expense ->
                val calendar = Calendar.getInstance()
                calendar.time = expense.date
                
                val currentDay = Calendar.getInstance()
                val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
                
                when {
                    isSameDay(calendar, currentDay) -> "Bugün"
                    isSameDay(calendar, yesterday) -> "Dün"
                    else -> {
                        val dateFormat = SimpleDateFormat("d MMMM", Locale("tr"))
                        dateFormat.format(expense.date)
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    
    // Harcama ekleme
    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }
    
    // Harcama güncelleme
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }
    
    // Harcama silme
    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }
    
    // ID'ye göre harcama getir
    suspend fun getExpenseById(id: String): Expense? {
        return repository.getExpenseById(id)
    }
    
    // Örnek veriler ekleme
    fun insertSampleData() {
        viewModelScope.launch {
            val sampleExpenses = listOf(
                Expense(
                    amount = 125.65,
                    description = "Puff&Bear",
                    type = com.example.expensetracker.model.ExpenseType.YEMEK,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -3) }.time,
                    people = 1
                ),
                Expense(
                    amount = 176.40,
                    description = "Testo shop",
                    type = com.example.expensetracker.model.ExpenseType.ALISVERIS,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -5) }.time,
                    people = 4
                ),
                Expense(
                    amount = 120.0,
                    description = "Rooms bar",
                    type = com.example.expensetracker.model.ExpenseType.EGLENCE,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -7) }.time,
                    people = 3
                ),
                Expense(
                    amount = 279.55,
                    description = "Lolita",
                    type = com.example.expensetracker.model.ExpenseType.YEMEK,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
                    people = 3
                ),
                Expense(
                    amount = 85.0,
                    description = "Taksi",
                    type = com.example.expensetracker.model.ExpenseType.ULASIM,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
                    people = 2
                ),
                Expense(
                    amount = 210.75,
                    description = "Market alışverişi",
                    type = com.example.expensetracker.model.ExpenseType.ALISVERIS,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
                    people = 1
                ),
                Expense(
                    amount = 550.0,
                    description = "Elektrik faturası",
                    type = com.example.expensetracker.model.ExpenseType.FATURA,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -6) }.time,
                    people = 1
                ),
                Expense(
                    amount = 350.0,
                    description = "Sinema",
                    type = com.example.expensetracker.model.ExpenseType.EGLENCE,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
                    people = 3
                ),
                Expense(
                    amount = 145.30,
                    description = "Öğle yemeği",
                    type = com.example.expensetracker.model.ExpenseType.YEMEK,
                    date = Calendar.getInstance().time,
                    people = 2
                ),
                Expense(
                    amount = 200.0,
                    description = "Kitap alışverişi",
                    type = com.example.expensetracker.model.ExpenseType.DIGER,
                    date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -4) }.time,
                    people = 1
                )
            )
            
            repository.insertExpenses(sampleExpenses)
        }
    }
    
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 