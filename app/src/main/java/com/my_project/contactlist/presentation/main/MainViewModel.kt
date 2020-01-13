package com.my_project.contactlist.presentation.main

import androidx.lifecycle.ViewModel
import com.my_project.contactlist.App
import com.my_project.contactlist.data.repository.MainRepository
import com.my_project.contactlist.router.Router
import com.my_project.contactlist.router.Screen
import javax.inject.Inject

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class MainViewModel: ViewModel() {

    @Inject
    lateinit var router: Router
    @Inject
    lateinit var mainRepository: MainRepository

    init {
        App.appComponent.inject(this)
    }

    fun showContacts() = router.replace(Screen.CONTACTS)

    fun back() = router.back()
}