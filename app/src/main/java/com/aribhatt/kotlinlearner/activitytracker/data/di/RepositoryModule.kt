package com.aribhatt.kotlinlearner.activitytracker.data.di

import com.aribhatt.kotlinlearner.activitytracker.data.db.RunDAO
import com.aribhatt.kotlinlearner.activitytracker.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideTrackerRepository(
        runDAO: RunDAO
    ): TrackerRepository {
        return TrackerRepository(runDAO)
    }
}