package com.my_project.contactlist

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.my_project.contactlist.di.*
import timber.log.Timber

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initComponent()
        initFresco()
        initTimber()
    }

    private fun initComponent() {
        appComponent = DaggerAppComponent.builder()
            .routerModule(RouterModule())
            .databaseModule(DatabaseModule(this))
            .repositoryModule(RepositoryModule())
            .build()
    }

    private fun initFresco() = Fresco.initialize(this)

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}