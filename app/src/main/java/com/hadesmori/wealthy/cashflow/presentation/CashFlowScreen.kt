package com.hadesmori.wealthy.cashflow.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

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

            OperationList(operations)

            Spacer(modifier = Modifier.weight(2f))
        }

        AddNewOperationButton(
            viewModel,
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
fun OperationList(operations: List<Operation>) {
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
            items(operations) {
                OperationItem(operation = it)
            }
        }
    }
}


//operation: Operation
//@Preview
@Composable
fun OperationItem(operation: Operation) {
    //val operation: Operation = Operation(null, "title", "description", OperationType.Income, LocalDate.now(), 1, 2)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .size(height = 60.dp, width = 330.dp)
            .background(color = Primary, shape = RoundedCornerShape(25.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_dollar),
            contentDescription = "itemIcon",
            Modifier
                .padding(horizontal = 8.dp)
                .size(48.dp),
            colorFilter = ColorFilter.tint(Color.White),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.Start) {
            Text(text = operation.label, color = Color.White)
            Text(text = operation.description, color = DarkerText)
        }
    }
}

@Composable
fun AddNewOperationButton(
    viewModel: CashFlowViewModel,
    modifier: Modifier,
    navigateToAddNewOperation: () -> Unit
) {
    Button(
        onClick = {
            navigateToAddNewOperation()
        }, modifier = modifier.padding(16.dp)
    )
    {
        Text(text = "+")
    }
}

fun nextProfile(viewModel: CashFlowViewModel) {
    if (currentProfileId < viewModel.profileCount.value) {
        currentProfileId += 1
    } else {
        currentProfileId = 1
    }
    viewModel.getProfileById(currentProfileId)
}

fun addNewOperation(viewModel: CashFlowViewModel) {
    val newOperation =
        Operation(null, "Lego", 10, "My favorite toy", OperationType.Income, LocalDate.now(), 1, 3)
    viewModel.addNewOperation(newOperation)
    viewModel.getOperations(currentProfileId)
    viewModel.getProfileById(currentProfileId)
}