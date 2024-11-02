package com.hadesmori.wealthy.cashflow.domain.model

import com.hadesmori.wealthy.cashflow.data.entities.ProfileEntity


data class Profile(
    val id: Long?,
    val name: String,
)

fun ProfileEntity.toDomain() = Profile(profileId, name)