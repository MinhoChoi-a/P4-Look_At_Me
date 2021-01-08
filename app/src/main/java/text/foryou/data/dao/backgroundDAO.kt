package text.foryou.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import text.foryou.data.model.BackgroundEntity

@Dao
interface backgroundDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSet(background: BackgroundEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(notes: List<BackgroundEntity>)

    //LiveData is kind of observer, so the data is updated automatically
    @Query("SELECT * FROM settings ORDER BY id ASC")
    fun getAll(): LiveData<List<BackgroundEntity>>

    @Query("SELECT * FROM settings WHERE id = :id")
    fun getSetById(id: Int): BackgroundEntity? //it is nullable

    @Query("SELECT * FROM settings WHERE res = :res")
    fun getSetByRes(res: String): BackgroundEntity? //it is nullable

    @Query("SELECT COUNT(*) FROM settings")
    fun getCount(): Int?
}