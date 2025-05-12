package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.ExpenseType
import com.example.expensetracker.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpenseCard(expense: Expense) {
    val (emoji, description) = when (expense.type) {
        ExpenseType.YEMEK -> Pair("🍔", "Yemek harcaması")
        ExpenseType.ULASIM -> Pair("🚕", "Ulaşım harcaması")
        ExpenseType.ALISVERIS -> Pair("🛒", "Alışveriş")
        ExpenseType.EGLENCE -> Pair("🎭", "Eğlence aktivitesi")
        ExpenseType.FATURA -> Pair("📄", "Fatura ödemesi")
        ExpenseType.DIGER -> Pair("📦", "Diğer harcama")
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sadece emoji 
            Text(
                text = emoji,
                fontSize = 34.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            
            // Açıklama ve kategori
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
            
            // Fiyat
            Text(
                text = "${expense.amount.toInt()} TL",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 