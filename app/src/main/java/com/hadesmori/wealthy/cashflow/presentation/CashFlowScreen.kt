package com.hadesmori.wealthy.cashflow.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.model.OperationType
import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.ui.theme.Primary
import java.time.LocalDate

var currentProfileId: Long = 2

@Composable
fun CashFlowScreen(
    viewModel: CashFlowViewModel = hiltViewModel()
) {
    viewModel.getProfileById(currentProfileId)
    viewModel.getOperations(currentProfileId)
    val profile = viewModel.profile.collectAsState().value
    val operations = viewModel.operations.collectAsState().value

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Profile(viewModel, profile)

            Spacer(modifier = Modifier.weight(1f))

            OperationList(operations)

            Spacer(modifier = Modifier.weight(2f))
        }

        Button(
            onClick = {
                addNewOperation(viewModel)
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
        {
            Text(text = "+")
        }
    }
}

fun addNewOperation(viewModel: CashFlowViewModel) {
    val newOperation = Operation(null,"food", "description", OperationType.Income, LocalDate.now(), 1, 2)
    viewModel.addNewOperation(newOperation)
}

@Composable
fun OperationList(operations: List<Operation>) {
    LazyColumn {
        items(operations) {
            OperationItem(operation = it)
        }
    }
}

@Composable
fun Profile(viewModel: CashFlowViewModel, profile: Profile) {
    Spacer(modifier = Modifier.padding(16.dp))
    Text(text = profile.name, textAlign = TextAlign.Center, color = Color.White)
    Spacer(modifier = Modifier.padding(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "120", fontSize = 40.sp, color = Color.White)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = "$", fontSize = 40.sp, color = Color.White)
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            nextProfile(viewModel)
        }) {
            Text(text = "+")
        }
    }
}

fun nextProfile(viewModel: CashFlowViewModel){
    if(currentProfileId < viewModel.profileCount.value)
    {
        currentProfileId += 1
    }
    else
    {
        currentProfileId = 1
    }
    viewModel.getProfileById(currentProfileId)
}

fun createProfile(viewModel: CashFlowViewModel, profileName: String) {
    val newProfile = Profile(null, profileName)
    viewModel.addNewProfile(newProfile)
}

@Composable
fun OperationItem(operation: Operation) {
    Text(text = operation.label)
}