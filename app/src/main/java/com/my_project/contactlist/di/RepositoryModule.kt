package com.my_project.contactlist.di

import com.my_project.contactlist.data.database.AppDatabase
import com.my_project.contactlist.data.repository.ChangeRepository
import com.my_project.contactlist.data.repository.ContactsRepository
import com.my_project.contactlist.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideContactsRepository(database: AppDatabase) = ContactsRepository(database)

    @Provides
    @Singleton
    fun provideChangeRepository(database: AppDatabase) = ChangeRepository(database)

    @Provides
    @Singleton
    fun provideMainRepository() = MainRepository()

}