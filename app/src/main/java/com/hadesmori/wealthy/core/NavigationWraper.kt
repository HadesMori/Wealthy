package com.hadesmori.wealthy.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hadesmori.wealthy.cashflow.presentation.CashFlowScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController)
{
    NavHost(navController = navHostController, startDestination = "cashFlow"){
        composable("initial")
        {
            /* Todo registration */
        }
        composable("cashFlow")
        {
            CashFlowScreen()
        }
    }
}