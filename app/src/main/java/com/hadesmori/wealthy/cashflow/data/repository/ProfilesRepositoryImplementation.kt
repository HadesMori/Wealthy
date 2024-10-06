package com.hadesmori.wealthy.cashflow.data.repository


import com.hadesmori.wealthy.cashflow.data.dao.CashFlowDao
import com.hadesmori.wealthy.cashflow.data.entities.toDatabase
import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.cashflow.domain.model.toDomain
import com.hadesmori.wealthy.cashflow.domain.repository.ProfilesRepository
import javax.inject.Inject

class ProfilesRepositoryImplementation @Inject constructor(
    private val cashFlowDao: CashFlowDao
) : ProfilesRepository {
    override suspend fun getProfile(profileId: Long): Profile {
        return cashFlowDao.getProfile(profileId).toDomain()
    }

    override suspend fun getAllProfiles(): List<Profile> {
        return cashFlowDao.getAllProfiles().map { it.toDomain() }
    }

    override suspend fun insertProfile(profile: Profile) {
        cashFlowDao.insertProfile(profile.toDatabase())
    }

    override suspend fun deleteProfile(profileId: Long){
        cashFlowDao.deleteProfile(profileId)
    }

    override suspend fun getProfileCount(): Int {
        return cashFlowDao.getProfileCount()
    }
}