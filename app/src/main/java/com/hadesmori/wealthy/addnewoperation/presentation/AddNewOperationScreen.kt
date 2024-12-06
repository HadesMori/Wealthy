package com.hadesmori.wealthy.addnewoperation.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hadesmori.wealthy.R
import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.model.OperationType
import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.cashflow.presentation.CashFlowViewModel
import com.hadesmori.wealthy.cashflow.presentation.currentProfileId
import com.hadesmori.wealthy.ui.theme.HintText
import com.hadesmori.wealthy.ui.theme.SecondaryVariant
import java.time.LocalDateTime

@Composable
fun AddNewOperationScreen(
    viewModel: CashFlowViewModel = hiltViewModel(),
    popBackStack: () -> Unit
) {
    // Trigger data collection
    viewModel.getProfileById(currentProfileId)
    viewModel.getAllProfiles()

    // Collect state values with null checks
    val profileState = viewModel.profile.collectAsState().value
    val allProfilesState = viewModel.profiles.collectAsState().value

    // Show loading UI until profile and allProfiles are loaded
    if (profileState != null && allProfilesState.isNotEmpty()) {

        var selectedAmount by remember { mutableStateOf(0f) }
        var selectedLabel by remember { mutableStateOf("") }
        var selectedDescription by remember { mutableStateOf("") }
        var selectedOperationType by remember { mutableStateOf(OperationType.Income) }
        var selectedProfile by remember { mutableStateOf(profileState) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SecondaryVariant)
        ) {
            TopBar(popBackStack)
            ConfigurationsSection(
                onAmountSelected = { selectedAmount = it },
                onOperationLabelSelected = { selectedLabel = it },
                onOperationDescriptionSelected = { selectedDescription = it },
                onOperationTypeSelected = { selectedOperationType = it },
                onProfileSelected = { selectedProfile = it },
                profileState,
                allProfilesState
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        )
        {
            ConfirmationButton(
                selectedAmount,
                selectedLabel,
                selectedDescription,
                selectedOperationType,
                selectedProfile,
                viewModel,
                popBackStack
            )
        }
    } else {
        // Display loading indicator while waiting for data
        LoadingScreen()
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Loading...", color = Color.White, fontSize = 16.sp)
    }
}


@Composable
fun TopBar(popBackStack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                Modifier.size(24.dp)
            )
        }
        Text(text = "New operation", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun ConfigurationsSection(
    onAmountSelected: (Float) -> Unit,
    onOperationLabelSelected: (String) -> Unit,
    onOperationDescriptionSelected: (String) -> Unit,
    onOperationTypeSelected: (OperationType) -> Unit,
    onProfileSelected: (Profile) -> Unit,
    profile: Profile,
    allProfiles: List<Profile>
) {
    AmountConfiguration(onAmountSelected)
    OperationLabel(onOperationLabelSelected)
    OperationDescription(onOperationDescriptionSelected)
    OperationType(onOperationTypeSelected)
    ChooseProfile(profile, allProfiles, onProfileSelected)
}

@Composable
fun AmountConfiguration(
    onAmountSelected: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "operationType",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(24.dp)
        )
        var text by remember { mutableStateOf(TextFieldValue("0")) }

        TextField(
            value = text,
            onValueChange = { input ->
                // Validate input: Only allow digits and a single dot
                val newText = input.text
                if (newText.count { it == '.' } <= 1 && newText.all { it.isDigit() || it == '.' }) {
                    text = input
                    val parsedAmount = newText.toFloatOrNull() ?: 0f
                    onAmountSelected(parsedAmount)
                }
            },
            placeholder = {
                Text("Amount", color = HintText)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Start
            )
        )

        CurrencyMenu()
    }
}

@Composable
fun CurrencyMenu() {
    var currencySymbol = remember { mutableStateOf("$") }
    Text(
        text = currencySymbol.value,
        color = Color.White,
        fontSize = 24.sp,
        modifier = Modifier.padding(start = 16.dp)
    )

    var isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    Button(
        onClick = { isDropDownExpanded.value = true },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .padding(start = 16.dp)
            .size(24.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_dropdown_arrow),
            contentDescription = "Currency",
            Modifier.size(24.dp),
            tint = Color.White
        )
    }

    // TODO: currency picker
    val currencies = listOf("$", "€", "₴")
    DropdownMenu(
        expanded = isDropDownExpanded.value,
        offset = DpOffset(240.dp, 0.dp),
        onDismissRequest = {
            isDropDownExpanded.value = false
        }
    ) {
        currencies.forEachIndexed { index, currency ->
            DropdownMenuItem(
                text = { Text(text = currency) },
                onClick = { currencySymbol.value = currency }
            )
        }
    }
}

@Composable
fun OperationLabel(
    onOperationLabelSelected: (String) -> Unit
) {
    val label = remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        TextField(
            value = label.value,
            onValueChange = {
                label.value = it
                onOperationLabelSelected(label.value)
            },
            placeholder = { Text(text = "Label", fontSize = 14.sp, color = HintText) },
            colors = TextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Start
            )
        )
    }
}

@Composable
fun OperationDescription(
    onOperationDescriptionSelected: (String) -> Unit
) {
    val description = remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        TextField(
            value = description.value,
            onValueChange = {
                description.value = it
                onOperationDescriptionSelected(description.value)
            },
            placeholder = { Text(text = "Description", fontSize = 14.sp, color = HintText) },
            colors = TextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Start
            )
        )
    }
}

@Composable
fun <T> DropdownMenuField(
    label: String,
    currentSelection: T,
    items: List<T>,
    itemLabel: (T) -> String,
    onSelect: (T) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = Color.White)

        var isDropDownExpanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .padding(end = 80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = itemLabel(currentSelection),
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Button(
                onClick = { isDropDownExpanded = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_dropdown_arrow),
                    contentDescription = label,
                    Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }

        DropdownMenu(
            expanded = isDropDownExpanded,
            offset = DpOffset(240.dp, 0.dp),
            onDismissRequest = { isDropDownExpanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = itemLabel(item)) },
                    onClick = {
                        onSelect(item)
                        isDropDownExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun OperationType(
    onOperationTypeSelected: (OperationType) -> Unit
) {
    var currentType by remember { mutableStateOf(OperationType.Income) }
    val operationTypes = OperationType.entries.toList()

    DropdownMenuField(
        label = "Operation type",
        currentSelection = currentType,
        items = operationTypes,
        itemLabel = { it.name },
        onSelect = { selectedType ->
            currentType = selectedType
            onOperationTypeSelected(currentType)
        }
    )
}

@Composable
fun ChooseProfile(
    profile: Profile,
    allProfiles: List<Profile>,
    onProfileSelected: (Profile) -> Unit
) {
    var currentProfile by remember { mutableStateOf(profile) }

    DropdownMenuField(
        label = "Profile",
        currentSelection = currentProfile,
        items = allProfiles,
        itemLabel = { it.name },
        onSelect = { selectedProfile ->
            currentProfile = selectedProfile
            onProfileSelected(currentProfile)
        }
    )
}

@Composable
fun ConfirmationButton(
    selectedAmount: Float,
    selectedLabel: String,
    selectedDescription: String,
    selectedOperationType: OperationType,
    selectedProfile: Profile,
    viewModel: CashFlowViewModel,
    popBackStack: () -> Unit
) {
    Button(
        onClick = {
            if(selectedLabel.isNotEmpty() && selectedAmount != 0f){
                viewModel.addNewOperation(
                    Operation(
                        null,
                        selectedLabel,
                        selectedAmount,
                        selectedDescription,
                        selectedOperationType,
                        LocalDateTime.now(),
                        1,
                        selectedProfile.id!!
                    )
                )
                popBackStack()
            }
        }
    ) {
        Text(text = "Create operation")
    }
}