package com.hadesmori.wealthy.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hadesmori.wealthy.cashflow.presentation.CashFlowScreen
import com.hadesmori.wealthy.addnewoperation.presentation.AddNewOperationScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController)
{
    NavHost(navController = navHostController, startDestination = CashFlow){
        composable("initial")
        {
            /* Todo registration */
        }
        composable<CashFlow>
        {
            CashFlowScreen { navHostController.navigate(AddNewOperation)}
        }
        composable<AddNewOperation>
        {
            AddNewOperationScreen { navHostController.popBackStack() }
        }
    }
}