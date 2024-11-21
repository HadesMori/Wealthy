package com.hadesmori.wealthy.cashflow.domain.model

import com.hadesmori.wealthy.cashflow.data.entities.OperationEntity
import java.time.LocalDate
import java.time.LocalDateTime

data class Operation(
    val id: Long?,
    val label: String = "",
    val amount: Float = 0f,
    val description: String = "",
    val type: OperationType,
    val date: LocalDateTime,
    val icon: Int,
    val profileId: Long
)

fun OperationEntity.toDomain() = Operation(operationId, label, amount, description, type, date, icon, profileId)