package com.example.vodo.di

import android.content.Context
import androidx.room.Room
import com.example.vodo.core.contants.TASK_DATABASE_NAME
import com.example.vodo.data.local.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TaskModule {

    @Singleton
    @Provides
    fun providesTaskDatabase(@ApplicationContext context: Context,): TaskDatabase {

        return Room.databaseBuilder(context, TaskDatabase::class.java, TASK_DATABASE_NAME).build()

    }


}