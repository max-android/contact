package com.my_project.contactlist.presentation.adapters

import com.my_project.contactlist.data.entities.Contact

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
class ContactMapper {

    fun mapDbToUI(contact: Contact): BaseType {
        return when {
            contact.type == "Друзья" -> ContactFriendType(
                contact.id,
                contact.type,
                contact.url,
                contact.surname,
                contact.name,
                contact.patronymic,
                contact.phone,
                contact.birthdate
            )
            contact.type == "Коллеги" -> ContactСolleagType(
                contact.id,
                contact.type,
                contact.url,
                contact.surname,
                contact.name,
                contact.patronymic,
                contact.phone,
                contact.position,
                contact.office_phone
            )
            else -> throw Exception("no exist entities")
        }
    }

    fun orderedList(contacts: List<Contact>): List<BaseType> {
        val friends = contacts.filter { it.type == "Друзья" }.map { item -> mapDbToUI(item) }
        val colleagues = contacts.filter { it.type == "Коллеги" }.map { item -> mapDbToUI(item) }
        val title1 = listOf(ContactTitleType("Друзья".hashCode(), "Друзья"))
        val title2 = listOf(ContactTitleType("Коллеги".hashCode(), "Коллеги"))
        if (friends.isEmpty()) {
            return title2.plus(colleagues)
        }
        if (colleagues.isEmpty()) {
            return title1.plus(friends)
        }
        val summList = title1.plus(friends).plus(title2).plus(colleagues)
        return summList
    }

    fun convertFriendTypeToContact(type: ContactFriendType): Contact {
        return Contact(
            type.id,
            type.type,
            0,
            type.url,
            type.surname,
            type.name,
            type.patronymic,
            type.phone,
            type.birthdate,
            null,
            null
        )
    }

    fun convertСolleagTypeToContact(type: ContactСolleagType): Contact {
        return Contact(
            type.id,
            type.type,
            0,
            type.url,
            type.surname,
            type.name,
            type.patronymic,
            type.phone,
            null,
            type.position,
            type.office_phone
        )
    }

}