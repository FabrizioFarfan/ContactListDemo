package com.example.contactlistappproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactlistappproject.db.ContactEntity
import com.example.contactlistappproject.repository.DatabaseRepository
import com.example.contactlistappproject.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelDB @Inject constructor(private val repository: DatabaseRepository) : ViewModel() {

    private val _contactList = MutableLiveData<DataStatus<List<ContactEntity>>>()
    val contactList: LiveData<DataStatus<List<ContactEntity>>>
        get() = _contactList

    private val _contactDetails = MutableLiveData<DataStatus<ContactEntity>>()
    val contactDetails : LiveData<DataStatus<ContactEntity>>
        get() = _contactDetails

    init {
        getAllContacts()
    }

    fun saveContact(isEdit:Boolean, contact: ContactEntity) = viewModelScope.launch {
        if (isEdit){
            repository.updateContact(contact)
        }else{
            repository.saveContact(contact)
        }
    }

    fun getAllContacts() = viewModelScope.launch {
        repository.getAllContacts()
            .catch {
                _contactList.postValue(DataStatus.error(it.message.toString()))
            }
            .collect{
                _contactList.postValue(DataStatus.success(it,it.isEmpty()))
            }
    }

    fun getSortedContactsAsc() = viewModelScope.launch {
        repository.sortedAsc()
            .catch {
                _contactList.postValue(DataStatus.error(it.message.toString()))
            }
            .collect{
                _contactList.postValue(DataStatus.success(it,it.isEmpty()))
            }
    }

    fun getSortedContactsDesc() = viewModelScope.launch {
        repository.sortedDesc()
            .catch {
                _contactList.postValue(DataStatus.error(it.message.toString()))
            }
            .collect{
                _contactList.postValue(DataStatus.success(it,it.isEmpty()))
            }
    }


    fun searchByName(name:String) = viewModelScope.launch {
        repository.searchByName(name)
            .collect{ _contactList.postValue(DataStatus.success(it,it.isEmpty())) }
    }




    fun deleteAllContacts() = viewModelScope.launch {
        repository.deleteAllContacts()
    }

    fun deleteContact(contact: ContactEntity) = viewModelScope.launch {
        repository.deleteContact(contact)
    }

    fun getContactToEdit (id: Int) = viewModelScope.launch {
        repository.getContactToEdit(id).collect{
            _contactDetails.postValue(DataStatus.success(it,false))
        }
    }

}