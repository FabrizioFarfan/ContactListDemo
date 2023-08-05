package com.example.contactlistappproject.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ContactEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ContactDB: RoomDatabase() {
    abstract fun contactDao() : ContactDao
}