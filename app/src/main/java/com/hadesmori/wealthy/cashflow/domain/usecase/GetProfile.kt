package com.hadesmori.wealthy.cashflow.domain.usecase

import android.util.Log
import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.cashflow.domain.repository.ProfilesRepository
import javax.inject.Inject

class GetProfile @Inject constructor(
    private val repository: ProfilesRepository
){
    suspend operator fun invoke(profileId: Long) : Profile {
        return repository.getProfile(profileId)
    }
}