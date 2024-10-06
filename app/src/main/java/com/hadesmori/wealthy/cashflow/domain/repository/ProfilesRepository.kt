package com.hadesmori.wealthy.cashflow.domain.repository

import com.hadesmori.wealthy.cashflow.domain.model.Operation
import com.hadesmori.wealthy.cashflow.domain.model.Profile

interface ProfilesRepository  {

    suspend fun getProfile(profileId: Long): Profile
    suspend fun getAllProfiles(): List<Profile>
    suspend fun insertProfile(profile: Profile)
    suspend fun deleteProfile(profileId: Long)
    suspend fun getProfileCount() : Int
}