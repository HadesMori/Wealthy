package com.hadesmori.wealthy.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hadesmori.wealthy.cashflow.data.dao.CashFlowDao
import com.hadesmori.wealthy.cashflow.data.entities.OperationEntity
import com.hadesmori.wealthy.cashflow.data.entities.ProfileEntity
import com.hadesmori.wealthy.util.DateConverters

@Database(
    entities = [OperationEntity::class, ProfileEntity::class],
    version = 3,
)
@TypeConverters(
    value = [DateConverters::class]
)
abstract class WealthyDatabase : RoomDatabase() {
    abstract fun getCashFlowDao(): CashFlowDao

    companion object{
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE operation_table ADD COLUMN 'amount' INT NOT NULL DEFAULT(0)")
            }
        }
    }
}