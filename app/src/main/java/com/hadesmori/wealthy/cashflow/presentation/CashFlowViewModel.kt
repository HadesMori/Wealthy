package com.hadesmori.wealthy.cashflow.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.model.OperationType
import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.cashflow.domain.usecase.AddOperation
import com.hadesmori.wealthy.cashflow.domain.usecase.DeleteOperation
import com.hadesmori.wealthy.cashflow.domain.usecase.DeleteProfile
import com.hadesmori.wealthy.cashflow.domain.usecase.GetOperationsFromProfile
import com.hadesmori.wealthy.cashflow.domain.usecase.GetProfile
import com.hadesmori.wealthy.cashflow.domain.usecase.GetProfileCount
import com.hadesmori.wealthy.cashflow.domain.usecase.GetProfiles
import com.hadesmori.wealthy.cashflow.domain.usecase.InsertProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashFlowViewModel @Inject constructor(
    private val getProfile: GetProfile,
    private val getProfiles: GetProfiles,
    private val insertProfile: InsertProfile,
    private val deleteProfile: DeleteProfile,
    private val getProfileCountCase: GetProfileCount,
    private val addOperation: AddOperation,
    private val getOperationsFromProfile: GetOperationsFromProfile,
    private val deleteOperation: DeleteOperation
) : ViewModel(){

    val profile = MutableStateFlow(Profile(null, ""))
    val profiles = MutableStateFlow(emptyList<Profile>())
    val profileCount = MutableStateFlow(0)
    val operations = MutableStateFlow<List<Operation>>(emptyList())

    init {
        getProfileCount()
        calculateAmounts()
    }

    //Profiles
    fun getProfileById(profileId: Long) : Profile{
        viewModelScope.launch{
            profile.value = getProfile(profileId)
        }
        return profile.value
    }

    fun getAllProfiles() : List<Profile>{
        viewModelScope.launch {
            profiles.value = getProfiles()
        }

        return profiles.value
    }

    fun addNewProfile(profile: Profile){
        viewModelScope.launch {
            insertProfile(profile)
        }
    }

    fun removeProfileById(profileId: Long){
        viewModelScope.launch {
            deleteProfile(profileId)
        }
    }

    fun getProfileCount() : Int{
        viewModelScope.launch {
            profileCount.value = getProfileCountCase()
        }
        return profileCount.value
    }

    //Operations
    fun addNewOperation(operation: Operation){
        viewModelScope.launch {
            addOperation(operation)
        }
    }

    fun getOperations(profileId: Long) : List<Operation>{
        viewModelScope.launch {
            operations.value = getOperationsFromProfile(profileId)
        }
        return operations.value
    }

    fun calculateAmounts() : Float {
        var amountOfMoney = 0f
        for (operation in operations.value){
            if(operation.type == OperationType.Income){
                amountOfMoney += operation.amount
            }
            else{
                amountOfMoney -= operation.amount
            }
        }
        return amountOfMoney
    }

    fun removeOperationById(operationId: Long?) {
        viewModelScope.launch {
            deleteOperation(operationId)
        }
    }
}