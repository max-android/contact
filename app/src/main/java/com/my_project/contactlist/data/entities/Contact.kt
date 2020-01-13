package com.my_project.contactlist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey val id: Int,
    val type:String,
    val typeInt:Int,
    val url:String?,
    val surname:String,
    val name:String,
    val patronymic:String?,
    val phone:String,
    val birthdate:String?,
    val position:String?,
    val office_phone:String?
) :Serializable



