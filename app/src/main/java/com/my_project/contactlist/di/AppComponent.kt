package com.my_project.contactlist.di

import com.my_project.contactlist.presentation.change.ChangeFragment
import com.my_project.contactlist.presentation.change.ChangeViewModel
import com.my_project.contactlist.presentation.contacts.ContactsFragment
import com.my_project.contactlist.presentation.contacts.ContactsViewModel
import com.my_project.contactlist.presentation.main.MainActivity
import com.my_project.contactlist.presentation.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
@Singleton
@Component
    (
    modules = [
        RouterModule::class,
        DatabaseModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(contactsFragment: ContactsFragment)
    fun inject(changeFragment: ChangeFragment)
    fun inject(mainViewModel: MainViewModel)
    fun inject(contactsViewModel: ContactsViewModel)
    fun inject(changeViewModel: ChangeViewModel)
}