package com.example.contactlistappproject.db


import androidx.room.*
import com.example.contactlistappproject.utils.Constants.CONTACT_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContact(contact: ContactEntity)

    @Query("SELECT * FROM $CONTACT_TABLE_NAME")
    fun getAllContacts(): Flow<MutableList<ContactEntity>>

    @Query("DELETE FROM $CONTACT_TABLE_NAME")
    fun deleteAllContacts()


    @Query("SELECT * FROM $CONTACT_TABLE_NAME ORDER BY name ASC")
    fun sortedAsc() : Flow<MutableList<ContactEntity>>


    @Query("SELECT * FROM $CONTACT_TABLE_NAME ORDER BY name DESC")
    fun sortedDesc() : Flow<MutableList<ContactEntity>>

    @Query("SELECT * FROM $CONTACT_TABLE_NAME WHERE name LIKE '%' || :name || '%' ")
    fun searchByName(name:String) :Flow<MutableList<ContactEntity>>

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("SELECT * FROM $CONTACT_TABLE_NAME WHERE id == :id")
    fun getContactToEdit(id: Int) : Flow<ContactEntity>


}