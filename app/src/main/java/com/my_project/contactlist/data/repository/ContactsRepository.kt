package com.my_project.contactlist.data.repository

import com.my_project.contactlist.data.database.AppDatabase
import com.my_project.contactlist.data.entities.Contact
import javax.inject.Inject

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ContactsRepository @Inject constructor(private val database: AppDatabase) {

    suspend fun contacts() = database.contactDAO().contacts()

    suspend fun deleteContact(id: Int) = database.contactDAO().deleteContact(id)

     suspend fun insertContact(contact: Contact) = database
        .contactDAO()
        .insertContact(contact)

}