package com.hadesmori.wealthy.core.di

import com.hadesmori.wealthy.cashflow.data.dao.CashFlowDao
import com.hadesmori.wealthy.cashflow.data.repository.OperationsRepositoryImplementation
import com.hadesmori.wealthy.cashflow.data.repository.ProfilesRepositoryImplementation
import com.hadesmori.wealthy.cashflow.domain.repository.OperationsRepository
import com.hadesmori.wealthy.cashflow.domain.repository.ProfilesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProfilesRepository(cashFlowDao: CashFlowDao): ProfilesRepository =
        ProfilesRepositoryImplementation(cashFlowDao)

    @Provides
    @Singleton
    fun provideOperationsRepository(cashFlowDao: CashFlowDao): OperationsRepository =
        OperationsRepositoryImplementation(cashFlowDao)
}