package com.hadesmori.wealthy.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hadesmori.wealthy.cashflow.data.dao.CashFlowDao
import com.hadesmori.wealthy.cashflow.data.entities.OperationEntity
import com.hadesmori.wealthy.cashflow.data.entities.ProfileEntity
import com.hadesmori.wealthy.util.DateConverters

@Database(
    entities = [OperationEntity::class, ProfileEntity::class],
    version = 2,
)
@TypeConverters(
    value = [DateConverters::class]
)
abstract class WealthyDatabase : RoomDatabase() {
    abstract fun getCashFlowDao(): CashFlowDao

}