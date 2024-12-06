package com.hadesmori.wealthy.cashflow.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hadesmori.wealthy.cashflow.data.entities.OperationEntity
import com.hadesmori.wealthy.cashflow.data.entities.ProfileEntity
import com.hadesmori.wealthy.cashflow.domain.model.Operation

@Dao
interface CashFlowDao {

    //Profiles
    @Query("SELECT * FROM profile_table WHERE profile_id = :profileId")
    suspend fun getProfile(profileId: Long): ProfileEntity

    @Query("SELECT * FROM profile_table")
    suspend fun getAllProfiles(): List<ProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Query("DELETE FROM profile_table WHERE profile_id = :profileId")
    suspend fun deleteProfile(profileId: Long)

    @Query("SELECT COUNT(*) FROM profile_table")
    suspend fun getProfileCount() : Int

    //Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOperation(operation: OperationEntity)

    @Query("SELECT * FROM operation_table WHERE profile_id = :profileId")
    suspend fun getOperationsFromProfile(profileId: Long) : List<OperationEntity>

    @Query("DELETE FROM operation_table WHERE operation_id = :operationId")
    suspend fun deleteOperation(operationId: Long)
}