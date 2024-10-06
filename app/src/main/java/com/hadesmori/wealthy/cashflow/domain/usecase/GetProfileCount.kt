package com.hadesmori.wealthy.cashflow.domain.usecase

import com.hadesmori.wealthy.cashflow.domain.repository.ProfilesRepository
import javax.inject.Inject

class GetProfileCount @Inject constructor(
    private val profilesRepository: ProfilesRepository
){
    suspend operator fun invoke() : Int{
        return profilesRepository.getProfileCount()
    }
}