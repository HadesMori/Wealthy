package com.hadesmori.wealthy.cashflow.domain.usecase

import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.repository.OperationsRepository
import javax.inject.Inject

class AddOperation @Inject constructor(
    private val repository: OperationsRepository
) {
    suspend operator fun invoke(operation: Operation){
        repository.addOperation(operation)
    }
}