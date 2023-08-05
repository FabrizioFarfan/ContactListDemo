package com.example.contactlistappproject.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.contactlistappproject.utils.Constants.CONTACT_TABLE_NAME

@Entity(tableName = CONTACT_TABLE_NAME)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String ="",
    var number: String=""
    )
