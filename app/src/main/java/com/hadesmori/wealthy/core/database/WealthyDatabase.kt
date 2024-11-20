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
    version = 4,
)
@TypeConverters(
    value = [DateConverters::class]
)
abstract class WealthyDatabase : RoomDatabase() {
    abstract fun getCashFlowDao(): CashFlowDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE operation_table ADD COLUMN 'amount' INT NOT NULL DEFAULT(0)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS operation_table_new (
                operation_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                label TEXT NOT NULL,
                amount REAL NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL,
                date TEXT NOT NULL,
                icon INTEGER NOT NULL,
                profile_id INTEGER NOT NULL,
                FOREIGN KEY(profile_id) REFERENCES profile_table(profile_id) ON UPDATE CASCADE ON DELETE CASCADE
            )
        """)

                db.execSQL("""
            INSERT INTO operation_table_new (operation_id, label, amount, description, type, date, icon, profile_id)
            SELECT operation_id, label, amount, description, type, date, icon, profile_id
            FROM operation_table
        """)

                db.execSQL("DROP TABLE operation_table")

                db.execSQL("ALTER TABLE operation_table_new RENAME TO operation_table")
            }
        }
    }
}