package com.hadesmori.wealthy.cashflow.domain.usecase

import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.cashflow.domain.repository.ProfilesRepository
import javax.inject.Inject

class GetProfiles @Inject constructor(
    val repository: ProfilesRepository
) {
    suspend operator fun invoke() : List<Profile>{
        return repository.getAllProfiles()
    }
}