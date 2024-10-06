package com.hadesmori.wealthy.cashflow.domain.usecase

import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.repository.OperationsRepository
import javax.inject.Inject

class GetOperationsFromProfile @Inject constructor(
    private val repository: OperationsRepository
) {
    suspend operator fun invoke(profileId: Long) : List<Operation>
    {
        return repository.getOperationsFromProfile(profileId)
    }
}