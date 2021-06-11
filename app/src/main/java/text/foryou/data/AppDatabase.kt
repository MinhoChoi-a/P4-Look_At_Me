package text.foryou.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import text.foryou.data.dao.FontColorDAO
import text.foryou.data.dao.FontStyleDAO
import text.foryou.data.dao.NoteDAO
import text.foryou.data.dao.backgroundDAO
import text.foryou.data.model.BackgroundEntity
import text.foryou.data.model.FontColorEntity
import text.foryou.data.model.FontStyleEntity
import text.foryou.data.model.NoteEntity


//Database constructor using RoomDatabase
@Database(entities=[NoteEntity::class, BackgroundEntity::class, FontColorEntity::class, FontStyleEntity::class],version=2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDAO?
    abstract fun setDao(): backgroundDAO?
    abstract fun fontColorDao(): FontColorDAO?
    abstract fun fontStyleDao(): FontStyleDAO?

    //singleton object which is not tied to the instance of the class
    companion object {

        private var INSTANCE: AppDatabase?= null

        //add(insert) updated data to the user's room data using Migration
        val MIGRATION_1_2 = object: Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("INSERT INTO settings (type, title, res, backImage) VALUES (3, 'Pink Heart', 'heart', 'heart')")
                database.execSQL("INSERT INTO settings (type, title, res, backImage) VALUES (3, 'Neon Line', 'neon', 'neon')")
                database.execSQL("INSERT INTO settings (type, title, res, backImage) VALUES (3, 'Heart Blossom', 'blossom', 'blossom')")
                database.execSQL("INSERT INTO settings (type, title, res, backImage) VALUES (3, 'Golden Age ', 'golden', 'golden')")
            }
        }

        //load database
        fun getInstance(context: Context): AppDatabase? {

            //when the database loaded initially
            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {

                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "LookAtMe1211.db"
                    )
                        //create data table for the first time
                        .addCallback(object: Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                ioThread {
                                    getInstance(context)?.setDao()?.insertAll(DefaultSetProvider.getSettings())
                                    getInstance(context)?.fontColorDao()?.insertAll(DefaultSetProvider.getFontColor())
                                    getInstance(context)?.fontStyleDao()?.insertAll(DefaultSetProvider.getFontStyle())
                                    getInstance(context)?.noteDao()?.insertAll(DefaultSetProvider.getNotes())
                                }

                            }
                        })
                        .addMigrations(MIGRATION_1_2) //check the version of data and do migration step
                        .build()
                }
            }
            return INSTANCE
        }
    }
}