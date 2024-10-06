package com.hadesmori.wealthy.cashflow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hadesmori.wealthy.cashflow.domain.model.Profile

@Entity(tableName = "profile_table")
data class ProfileEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("profile_id") val profileId : Long = 0,

    @ColumnInfo("name") val name: String,
)

fun Profile.toDatabase() = ProfileEntity(name = name)