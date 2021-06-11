package text.foryou.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import text.foryou.data.model.FontColorEntity

@Dao
interface FontColorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fontColor: FontColorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(fontColors: List<FontColorEntity>)

    //LiveData is kind of observer, so the data is updated automatically
    @Query("SELECT * FROM fontscolor ORDER BY id ASC")
    fun getAll(): LiveData<List<FontColorEntity>>

    @Query("SELECT * FROM fontscolor WHERE id = :id")
    fun getfontById(id: Int): FontColorEntity?

    @Query("SELECT COUNT(*) from fontscolor")
    fun getCount(): Int

    @Query("SELECT * FROM fontscolor where color = :color")
    fun getfontByColor(color: String): FontColorEntity?

    @Query("SELECT id FROM fontscolor where color = :color")
    fun getfontIdByColor(color: String): Int
}