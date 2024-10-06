package com.hadesmori.wealthy.core.di

import android.content.Context
import androidx.room.Room
import com.hadesmori.wealthy.core.database.WealthyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "wealthy_database"

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, WealthyDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCashFlowDao(db: WealthyDatabase) = db.getCashFlowDao()
}