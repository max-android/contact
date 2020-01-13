package com.my_project.contactlist.presentation.contacts

import com.my_project.contactlist.data.entities.Contact

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
sealed class ContactViewState
class SuccessContacts(val contacts: List<Contact>):ContactViewState()
class Error(val error: Throwable):ContactViewState()
