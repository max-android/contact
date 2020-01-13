package com.my_project.contactlist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my_project.contactlist.data.entities.Contact

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
@Database(
    entities = [Contact::class], version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDAO(): ContactDAO
}