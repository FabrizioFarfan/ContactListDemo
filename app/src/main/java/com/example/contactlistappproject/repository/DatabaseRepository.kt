package com.example.contactlistappproject.repository

import com.example.contactlistappproject.db.ContactDao
import com.example.contactlistappproject.db.ContactEntity
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val dao: ContactDao
) {
    suspend fun saveContact(contact: ContactEntity) = dao.saveContact(contact)

    fun getAllContacts() = dao.getAllContacts()

    fun deleteAllContacts() = dao.deleteAllContacts()

    fun sortedAsc() = dao.sortedAsc()

    fun sortedDesc() = dao.sortedDesc()

    fun searchByName(name : String) = dao.searchByName(name)

    suspend fun updateContact(contact: ContactEntity) = dao.updateContact(contact)

    suspend fun deleteContact(contact: ContactEntity) = dao.deleteContact(contact)

    fun getContactToEdit(id: Int) = dao.getContactToEdit(id)
}