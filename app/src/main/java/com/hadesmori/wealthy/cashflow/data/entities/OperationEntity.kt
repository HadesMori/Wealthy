package com.hadesmori.wealthy.cashflow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.model.OperationType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Entity(
    tableName = "operation_table",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = arrayOf("profile_id"),
            childColumns = arrayOf("profile_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OperationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("operation_id")
    val operationId: Long = 0,

    @ColumnInfo("label")
    val label: String,
    @ColumnInfo("amount")
    val amount: Float = 0f,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("type")
    val type: OperationType,
    @ColumnInfo("date")
    val date: LocalDateTime,
    @ColumnInfo("icon")
    val icon: Int,
    @ColumnInfo("profile_id")
    val profileId: Long
)

fun Operation.toDatabase() = OperationEntity(label = label, amount = amount, description = description, type = type, date = date, icon = icon, profileId = profileId)