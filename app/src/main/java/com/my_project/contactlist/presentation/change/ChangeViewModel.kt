package com.my_project.contactlist.presentation.change

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my_project.contactlist.App
import com.my_project.contactlist.data.entities.Contact
import com.my_project.contactlist.data.repository.ChangeRepository
import com.my_project.contactlist.router.Router
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ChangeViewModel: ViewModel() {


    @Inject lateinit var repository: ChangeRepository
    @Inject lateinit var router: Router
    var vmTypeContact:String = ""
    var vmUrlProfile:String? = null
    var vmSurname:String? = null
    var vmName:String? = null
    var vmPatronymic:String? = null
    var vmPhone:String? = null
    var vmBirthdate:String? = null
    var vmPosition:String? = null
    var vm_office_phone:String? = null
    var phone_contact:String = ""
    var phone_contact_id:String = ""

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    init {
        App.appComponent.inject(this)
    }

    fun saveContact(contact:Contact){
        Timber.tag("--CONTACT-saveContact").i("------" + contact.toString())
        viewModelScope.launch(exceptionHandler) {
             withContext(Dispatchers.IO){
                repository.insertContact(contact)
            }
            router.back()
        }
    }

    fun deleteContact(id:Int){
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO){
                repository.deleteContact(id)
            }
        }
    }

    fun saveInPhoneContact(contact:Contact,contentResolver:ContentResolver){

        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO){

                Timber.tag("--CONTACT-result-contact").i("------" + contact.toString())


                val ops = ArrayList<ContentProviderOperation>()
                ops.add(
                    ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                            ContactsContract.Data.CONTACT_ID + " = ? AND "
                                    + ContactsContract.Data.MIMETYPE + " = ?",
                            arrayOf(contact.id.toString(), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE))
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.name)
                        .build()
                )
                val result = contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)

                Timber.tag("--CONTACT-result-n").i("------" + result.size.toString())

                ops.clear()
                ops.add(
                    ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                            ContactsContract.Data.CONTACT_ID + " = ? AND "
                                    + ContactsContract.Data.MIMETYPE + " = ?",
                            arrayOf(contact.id.toString(), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.phone)
                        .build()
                )

                val result2 = contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
                Timber.tag("--CONTACT-result-p").i("------" + result2.size.toString())
                ops.clear()
            }
       }

    }


}