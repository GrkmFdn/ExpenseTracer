package com.example.expensetracker.data.repository

import com.example.expensetracker.data.database.ExpenseDao
import com.example.expensetracker.model.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    
    // Tüm harcamaları getir
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    
    // Harcama ekle
    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }
    
    // Birden fazla harcama ekle
    suspend fun insertExpenses(expenses: List<Expense>) {
        expenseDao.insertExpenses(expenses)
    }
    
    // Harcama güncelle
    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }
    
    // Harcama sil
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }
    
    // ID'ye göre harcama getir
    suspend fun getExpenseById(id: String): Expense? {
        return expenseDao.getExpenseById(id)
    }
    
    // İki tarih arasındaki harcamaları getir
    fun getExpensesBetweenDates(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesBetweenDates(startDate, endDate)
    }
    
    // İki tarih arasındaki toplam harcama miktarını getir
    fun getTotalExpenseBetweenDates(startDate: Date, endDate: Date): Flow<Double?> {
        return expenseDao.getTotalExpenseBetweenDates(startDate, endDate)
    }
    
    // Belirli bir tarihten sonraki harcamaları getir
    fun getExpensesAfterDate(date: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesAfterDate(date)
    }
    
    // Haftanın başlangıç tarihini hesapla
    fun getStartOfWeek(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    // Haftanın bitiş tarihini hesapla
    fun getEndOfWeek(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek + 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    
    // Bu haftanın harcamalarını getir
    fun getCurrentWeekExpenses(): Flow<List<Expense>> {
        return getExpensesBetweenDates(getStartOfWeek(), getEndOfWeek())
    }
    
    // Bu haftanın toplam harcama miktarını getir
    fun getCurrentWeekTotalExpense(): Flow<Double?> {
        return getTotalExpenseBetweenDates(getStartOfWeek(), getEndOfWeek())
    }
} 