package com.hadesmori.wealthy.cashflow.domain.usecase

import com.hadesmori.wealthy.cashflow.domain.repository.OperationsRepository
import javax.inject.Inject

class DeleteOperation @Inject constructor(
    private val repository: OperationsRepository
) {

    suspend operator fun invoke(operationId: Long?){
        repository.deleteOperation(operationId)
    }
}