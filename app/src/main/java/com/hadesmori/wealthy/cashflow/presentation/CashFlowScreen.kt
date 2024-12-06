package com.hadesmori.wealthy.cashflow.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hadesmori.wealthy.R
import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.model.OperationType
import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.ui.theme.DarkerText
import com.hadesmori.wealthy.ui.theme.Primary
import com.hadesmori.wealthy.ui.theme.Secondary
import com.hadesmori.wealthy.ui.theme.SecondaryVariant
import java.time.format.DateTimeFormatter

var currentProfileId: Long = 2

@Composable
fun CashFlowScreen(
    viewModel: CashFlowViewModel = hiltViewModel(),
    navigateToAddNewOperation: () -> Unit,
) {
    updateUI(viewModel)
    val profiles = viewModel.getAllProfiles()
    val operations = viewModel.operations.collectAsState().value

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            CashFlowTopBar()

            //Profile horizontal pager
            val pagerState = rememberPagerState(pageCount = { viewModel.getProfileCount() })
            HorizontalPager(state = pagerState) { page ->
                Profile(viewModel, profiles[page])
                currentProfileId = profiles[pagerState.currentPage].id!!
                updateUI(viewModel)
            }

            OperationList(operations, viewModel)

            Spacer(modifier = Modifier.weight(2f))
        }

        AddNewOperationButton(
            Modifier.align(Alignment.BottomEnd),
            navigateToAddNewOperation
        )
    }
}

@Composable
fun CashFlowTopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            /* TODO addNewProfile function */
        },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Image(painter = painterResource(R.drawable.ic_add), contentDescription = "Add new profile")
        }
    }
}

fun updateUI(viewModel: CashFlowViewModel) {
    viewModel.getProfileById(currentProfileId)
    viewModel.getOperations(currentProfileId)
}

@Composable
fun Profile(viewModel: CashFlowViewModel, profile: Profile) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = profile.name,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = viewModel.calculateAmounts().toString(), fontSize = 48.sp, color = Color.White)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "$", fontSize = 48.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.padding(32.dp))
    }
}

@Composable
fun OperationList(operations: List<Operation>, viewModel: CashFlowViewModel) {
    val colorStops = arrayOf(
        0.0f to SecondaryVariant,
        1.0f to Secondary
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colorStops = colorStops),
                shape = RoundedCornerShape(25.dp)
            )
    ) {
        LazyColumn(Modifier.padding(vertical = 8.dp)) {
            items(operations.sortedByDescending { it.date }) {
                OperationItem(operation = it, viewModel = viewModel)
            }
        }
    }
}

//operation: Operation
//@Preview
@Composable
fun OperationItem(operation: Operation, viewModel: CashFlowViewModel) {
    val shouldShowDialog = remember { mutableStateOf(false) }

    DeleteOperationAlertDialog(shouldShowDialog, viewModel, operation)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .size(height = 60.dp, width = 330.dp)
            .background(color = Primary, shape = RoundedCornerShape(25.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        shouldShowDialog.value = true
                    }
                )
            }
    ) {
        Row{
            Image(
                painter = painterResource(id = R.drawable.ic_dollar),
                contentDescription = "itemIcon",
                Modifier
                    .padding(horizontal = 8.dp)
                    .size(48.dp),
                colorFilter = ColorFilter.tint(Color.White),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp), horizontalAlignment = Alignment.Start) {
                Text(text = operation.label, color = Color.White)
                Text(text = if(operation.description.isNotEmpty()){
                    operation.description
                }
                    else
                {
                    stringResource(R.string.empty_operation_description_text)
                }
                    , color = DarkerText)
            }
        }

        //TODO
        val signedAmount = when(operation.type){
            OperationType.Income -> "+" + operation.amount.toString() + "$"
            OperationType.Expense -> "-" + operation.amount.toString() + "$"
            else -> operation.amount.toString() + "$"
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp), horizontalAlignment = Alignment.Start, modifier = Modifier.size(width = 80.dp, height = 48.dp)) {
            Text(text = signedAmount, color = Color.White)
            Text(text = operation.date.format(DateTimeFormatter.ofPattern("dd-MM")), color = DarkerText)
        }
    }
}

@Composable
fun AddNewOperationButton(
    modifier: Modifier,
    navigateToAddNewOperation: () -> Unit
) {
    Button(
        onClick = {
            navigateToAddNewOperation()
        },
        modifier = modifier.padding(16.dp).size(50.dp),
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Primary)
    )
    {
        Image(painterResource(R.drawable.ic_add), contentDescription = "Add operation")
    }
}

@Composable
fun DeleteOperationAlertDialog(shouldShowDialog: MutableState<Boolean>, viewModel: CashFlowViewModel, operation: Operation) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },
            title = { Text(text = stringResource(R.string.alert_dialog_delete_operation_title)) },
            text = { Text(text = stringResource(R.string.alert_dialog_delete_operation_text)) },
            confirmButton = {
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                        viewModel.removeOperationById(operation.id)
                        updateUI(viewModel)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(
                        text = "Dismiss",
                        color = Color.White
                    )
                }
            }
        )
    }
}