package com.my_project.contactlist.di

import com.my_project.contactlist.router.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
@Module
class RouterModule {
    @Provides
    @Singleton
    fun provideRouter() = Router()
}