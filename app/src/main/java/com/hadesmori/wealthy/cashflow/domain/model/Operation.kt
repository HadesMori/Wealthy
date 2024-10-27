package com.hadesmori.wealthy.cashflow.domain.model

import com.hadesmori.wealthy.cashflow.data.entities.OperationEntity
import java.time.LocalDate

data class Operation(
    val id: Long?,
    val label: String = "",
    val amount: Int = 0,
    val description: String = "",
    val type: OperationType,
    val date: LocalDate,
    val icon: Int,
    val profileId: Long
)

fun OperationEntity.toDomain() = Operation(operationId, label, amount, description, type, date, icon, profileId)