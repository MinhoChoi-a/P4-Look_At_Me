package com.example.lookatme.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[NoteEntity::class],version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDAO?

    companion object {

        private var INSTANCE: AppDatabase?= null

        fun getInstance(context: Context):AppDatabase? {

            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "notes.db"
                    ).build()
                }
            }

            return INSTANCE
        }


    }


}