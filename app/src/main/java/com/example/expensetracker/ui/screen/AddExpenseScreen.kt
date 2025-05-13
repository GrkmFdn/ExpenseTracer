package com.example.expensetracker.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.ExpenseApplication
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.ExpenseType
import com.example.expensetracker.ui.theme.Primary
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel
import com.example.expensetracker.ui.viewmodel.ExpenseViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val repository = (context.applicationContext as ExpenseApplication).repository
    val viewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(repository)
    )
    
    val scope = rememberCoroutineScope()
    
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ExpenseType.YEMEK) }
    var personCount by remember { mutableStateOf("1") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni Harcama Ekle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (amount.isNotEmpty() && description.isNotEmpty()) {
                        val expense = Expense(
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            description = description,
                            type = selectedType,
                            date = Date(),
                            people = personCount.toIntOrNull() ?: 1
                        )
                        
                        scope.launch {
                            viewModel.insertExpense(expense)
                            onNavigateBack()
                        }
                    }
                },
                containerColor = Primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Check, contentDescription = "Kaydet")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Miktar alanı
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Miktar (TL)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            
            // Açıklama alanı
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Açıklama") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Harcama tipi seçimi
            Text("Harcama Tipi", style = MaterialTheme.typography.titleMedium)
            ExpenseTypeSelector(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it }
            )
            
            // Kişi sayısı alanı
            OutlinedTextField(
                value = personCount,
                onValueChange = { personCount = it },
                label = { Text("Kişi Sayısı") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    }
}

@Composable
fun ExpenseTypeSelector(
    selectedType: ExpenseType,
    onTypeSelected: (ExpenseType) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ExpenseType.values().forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Primary
                    )
                )
                
                Text(
                    text = when (type) {
                        ExpenseType.YEMEK -> "Yemek"
                        ExpenseType.ULASIM -> "Ulaşım"
                        ExpenseType.ALISVERIS -> "Alışveriş" 
                        ExpenseType.EGLENCE -> "Eğlence"
                        ExpenseType.FATURA -> "Fatura"
                        ExpenseType.DIGER -> "Diğer"
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = when (type) {
                        ExpenseType.YEMEK -> "🍔"
                        ExpenseType.ULASIM -> "🚕"
                        ExpenseType.ALISVERIS -> "🛒"
                        ExpenseType.EGLENCE -> "🎭"
                        ExpenseType.FATURA -> "📄"
                        ExpenseType.DIGER -> "📦"
                    },
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }
        }
    }
} 