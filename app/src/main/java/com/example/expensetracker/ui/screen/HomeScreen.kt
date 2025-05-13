package com.example.expensetracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.ExpenseApplication
import com.example.expensetracker.R
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.ExpenseType
import com.example.expensetracker.ui.components.ExpenseCard
import com.example.expensetracker.ui.theme.Primary
import com.example.expensetracker.ui.theme.Surface
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel
import com.example.expensetracker.ui.viewmodel.ExpenseViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddExpense: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = (context.applicationContext as ExpenseApplication).repository
    val viewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(repository)
    )
    
    // Verileri topla
    val allExpenses by viewModel.allExpenses.collectAsState()
    val groupedExpenses by viewModel.groupedExpenses.collectAsState()
    val currentWeekTotalExpense by viewModel.currentWeekTotalExpense.collectAsState()
    
    // Gerekirse örnek verileri yükle (sadece ilk çalıştırmada)
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        if (allExpenses.isEmpty()) {
            scope.launch {
                viewModel.insertSampleData()
            }
        }
    }
    
    var selectedTab by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Merhaba, Görkem",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    },
                    actions = {
                        // Profil Avatar
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .padding(end = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                modifier = Modifier
                                    .size(60.dp)
                                    .border(2.dp, Primary, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar_placeholder),
                                    contentDescription = "Profil Resmi",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    )
                )
            },
            bottomBar = {
                // Sadece bottom içerik (hiçbir konteyner yok)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(bottom = 8.dp)
                ) {
                    // İkonlar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ana sayfa ikonu - seçili durumda
                        IconButton(
                            onClick = { /* Ana sayfa */ },
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.GridView,
                                contentDescription = "Ana Sayfa",
                                tint = Primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        
                        // Grafik ikonu
                        IconButton(
                            onClick = { /* Grafikler */ },
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Grafikler",
                                tint = Color.Gray,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Haftalık Toplam Harcama
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bu Hafta",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )
                    
                    Text(
                        text = "${currentWeekTotalExpense.toInt()} TL",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 42.sp, 
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                // Son Harcamalar Başlığı
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Son harcamalar",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = { /* Tüm harcamaları göster */ }) {
                        Text(
                            text = "Tümünü gör",
                            color = Primary
                        )
                    }
                }
                
                // Harcama Listesi
                LazyColumn {
                    if (groupedExpenses.isEmpty() && allExpenses.isNotEmpty()) {
                        // Gruplandırma henüz yapılmadıysa tüm harcamaları göster
                        items(allExpenses.sortedByDescending { it.date }) { expense ->
                            ExpenseCard(expense = expense)
                        }
                    } else {
                        // Tarihe göre gruplandırılmış veriler
                        groupedExpenses.forEach { (date, expenses) ->
                            item {
                                Text(
                                    text = date,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
                                )
                            }
                            
                            items(expenses) { expense ->
                                ExpenseCard(expense = expense)
                            }
                        }
                    }
                    
                    // ListeEnAltına Padding ekliyorum ki add butonu içeriği örtmesin
                    item {
                        Spacer(modifier = Modifier.height(70.dp))
                    }
                }
            }
        }
        
        // Ortada konumlandırılmış FAB (diğer tüm elementlerin üzerinde)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-16).dp)
                .zIndex(10f)
        ) {
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(16.dp, CircleShape)
                    .clip(CircleShape),
                color = Primary
            ) {
                IconButton(
                    onClick = onAddExpense,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Default.Add, 
                        contentDescription = "Harcama Ekle",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabItem(
    selected: Boolean,
    title: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { 
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon, 
                contentDescription = title,
                modifier = Modifier.size(28.dp)
            ) 
        },
        label = { 
            Text(
                text = title,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            ) 
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Primary,
            selectedTextColor = Primary,
            indicatorColor = Color.White
        )
    )
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
} 