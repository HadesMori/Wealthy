package com.hadesmori.wealthy.cashflow.domain.usecase

import com.hadesmori.wealthy.cashflow.domain.model.Profile
import com.hadesmori.wealthy.cashflow.domain.repository.ProfilesRepository
import javax.inject.Inject

class DeleteProfile @Inject constructor(
    private val repository: ProfilesRepository
) {
    suspend operator fun invoke(profileId: Long){
        repository.deleteProfile(profileId)
    }
}