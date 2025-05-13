package com.example.expensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.screen.AddExpenseScreen
import com.example.expensetracker.ui.screen.HomeScreen

object Destinations {
    const val HOME_ROUTE = "home"
    const val ADD_EXPENSE_ROUTE = "add_expense"
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.HOME_ROUTE) {
            HomeScreen(
                onAddExpense = {
                    navController.navigate(Destinations.ADD_EXPENSE_ROUTE)
                }
            )
        }
        
        composable(Destinations.ADD_EXPENSE_ROUTE) {
            AddExpenseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 