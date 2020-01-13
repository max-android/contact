package com.my_project.contactlist.presentation.adapters

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
sealed class BaseType {
    fun getItemId() = when (this) {
        is ContactTitleType -> this.id
        is ContactFriendType -> this.id
        is ContactСolleagType -> this.id
    }
}

data class ContactTitleType(val id: Int, val title: String) : BaseType()

data class ContactFriendType(val id: Int,
                             val type: String,
                             val url:String?,
                             val surname: String,
                             val name: String,
                             val patronymic: String?,
                             val phone: String,
                             val birthdate: String?
                             ) : BaseType()

data class ContactСolleagType(val id: Int,
                              val type: String,
                              val url:String?,
                              val surname: String,
                              val name: String,
                              val patronymic: String?,
                              val phone: String,
                              val position:String?,
                              val office_phone:String?
                              ) : BaseType()

