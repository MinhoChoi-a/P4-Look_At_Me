package text.foryou.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities=[NoteEntity::class, BackgroundEntity::class, FontColorEntity::class, FontStyleEntity::class],version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDAO?
    abstract fun setDao(): backgroundDAO?
    abstract fun fontColorDao(): FontColorDAO?
    abstract fun fontStyleDao(): FontStyleDAO?

    companion object {

        private var INSTANCE: AppDatabase?= null

        val MIGRATION_1_2 = object: Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }

        fun getInstance(context: Context): AppDatabase? {

            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "updateTest2.db"
                    )
                        //.addMigrations(MIGRATION_1_2)
                        //.setQueryExecutor()

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
                        .build()
                }
            }

            return INSTANCE
        }
    }




}