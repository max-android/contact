package com.my_project.contactlist.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.my_project.contactlist.data.entities.Contact

/**
 * Created Максим on 31.10.2019.
 * Copyright © Max
 */
@Dao
interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Query("SELECT * FROM contact ORDER BY typeInt ASC")
    suspend fun contacts():List<Contact>

    @Query("DELETE FROM contact WHERE id = :selectId")
    suspend fun deleteContact(selectId: Int)

}