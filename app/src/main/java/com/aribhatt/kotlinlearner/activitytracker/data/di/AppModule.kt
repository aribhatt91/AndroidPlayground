package com.aribhatt.kotlinlearner.activitytracker.data.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.KEY_FIRST_TIME_TOGGLE
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.KEY_NAME
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.KEY_WEIGHT
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.RUN_DATABASE_NAME
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.SHARED_PREFERENCES_NAME
import com.aribhatt.kotlinlearner.activitytracker.data.db.RunDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//AppModule Provide all of our dependencies
@Module
@InstallIn(SingletonComponent::class)
//The dependencies are created when the application is created and
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunDatabase::class.java,
        RUN_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunDatabase) = db.getRunDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext app: Context
    ) = app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideName(
        sharedPref: SharedPreferences
    ):String? = sharedPref.getString(KEY_NAME, "")

    @Provides
    @Singleton
    fun provideWeight(
        sharedPref: SharedPreferences
    ):Float = sharedPref.getFloat(KEY_WEIGHT, 80f)

    @Provides
    @Singleton
    fun provideFirstRun(
        sharedPref: SharedPreferences
    ):Boolean = sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}