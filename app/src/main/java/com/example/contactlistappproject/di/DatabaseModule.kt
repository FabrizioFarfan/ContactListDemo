package com.example.contactlistappproject.di

import android.content.Context
import androidx.room.Room
import com.example.contactlistappproject.db.ContactDB
import com.example.contactlistappproject.db.ContactEntity
import com.example.contactlistappproject.utils.Constants.CONTACT_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,ContactDB::class.java,CONTACT_DATABASE
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun providesDao(db: ContactDB) = db.contactDao()

    @Provides
    @Singleton
    fun providesContact() = ContactEntity()
}