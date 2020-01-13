package com.my_project.contactlist.di

import android.content.Context
import androidx.room.Room
import com.my_project.contactlist.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
@Module
class DatabaseModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "db_store")
            .build()
    }

}