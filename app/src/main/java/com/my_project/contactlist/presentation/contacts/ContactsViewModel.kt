package com.my_project.contactlist.presentation.contacts

import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my_project.contactlist.App
import com.my_project.contactlist.data.entities.Contact
import com.my_project.contactlist.data.repository.ContactsRepository
import com.my_project.contactlist.presentation.adapters.*
import com.my_project.contactlist.router.Router
import com.my_project.contactlist.router.Screen
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ContactsViewModel : ViewModel() {

    @Inject
    lateinit var router: Router
    @Inject
    lateinit var repository: ContactsRepository
    val liveData = MutableLiveData<ContactViewState>()

    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            liveData.value = Error(throwable)
        }

    init {
        App.appComponent.inject(this)
    }

    fun contacts() {
        viewModelScope.launch(exceptionHandler) {
            val contacts = withContext(Dispatchers.IO) {
                repository.contacts()
            }
            if (contacts.isNotEmpty()) {
                liveData.value = SuccessContacts(contacts)
            }
        }
    }

    fun deleteContact(type: BaseType) {
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                when (type) {
                    is ContactFriendType -> repository.deleteContact(type.id)
                    is ContactСolleagType -> repository.deleteContact(type.id)
                    is ContactTitleType -> {
                    }
                }
            }
            contacts()
        }
    }


    fun loadFromPhone(contentResolver: ContentResolver) {

        viewModelScope.launch(exceptionHandler) {
            val contacts = withContext(Dispatchers.IO) {
                val cursor = contentResolver
                    .query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        arrayOf(
                             ContactsContract.Data.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                            ContactsContract.Data.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        ),
                        null, null, null
                    )

                cursor?.let {
                    if (it.count > 0) {
                        while (it.moveToNext()) {
                            val id = it.getString(0)
                            val uri = it.getString(1)
                            val name = it.getString(2)
                            val phone = it.getString(3)

                            repository.insertContact(
                                Contact(
                                    id.toInt(), "Друзья",
                                    0, uri, "Фамилия", name, null, phone,
                                    null, null, null
                                )
                            )
                        }
                    }
                    it.close()
                }

                   repository.contacts()

            }
            if (contacts.isNotEmpty()) {
                liveData.value = SuccessContacts(contacts)
            }
        }

    }

    fun addContact() = router.forward(Screen.CHANGE)

    fun editContact(type: BaseType) {
        when (type) {
            is ContactFriendType -> router.forward(
                Screen.CHANGE,
                true,
                ContactMapper().convertFriendTypeToContact(type)
            )
            is ContactСolleagType -> router.forward(
                Screen.CHANGE,
                true,
                ContactMapper().convertСolleagTypeToContact(type)
            )
            is ContactTitleType -> {
            }
        }
    }

}