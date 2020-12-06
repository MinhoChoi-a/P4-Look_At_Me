package com.example.lookatme.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities=[NoteEntity::class, SetEntity::class, FontEntity::class],version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDAO?
    abstract fun setDao(): SetDAO?
    abstract fun fontDao(): FontDAO?

    companion object {

        private var INSTANCE: AppDatabase?= null

        fun getInstance(context: Context):AppDatabase? {

            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "LookAtMe1204.db"
                    ).build()
                }
            }
            return INSTANCE
        }


    }


}