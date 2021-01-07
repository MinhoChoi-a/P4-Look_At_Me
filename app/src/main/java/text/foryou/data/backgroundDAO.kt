package text.foryou.data

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Query("SELECT COUNT(*) FROM settings")
    fun getCount(): Int?
}