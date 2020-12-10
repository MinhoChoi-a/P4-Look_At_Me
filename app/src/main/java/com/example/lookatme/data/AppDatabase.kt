package com.example.lookatme.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[NoteEntity::class, BackgroundEntity::class, FontColorEntity::class, FontStyleEntity::class],version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDAO?
    abstract fun setDao(): backgroundDAO?
    abstract fun fontColorDao(): FontColorDAO?
    abstract fun fontStyleDao(): FontStyleDAO?

    companion object {

        private var INSTANCE: AppDatabase?= null

        fun getInstance(context: Context):AppDatabase? {

            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "LookAtMe1210.db"
                    ).build()
                }
            }
            return INSTANCE
        }


    }


}