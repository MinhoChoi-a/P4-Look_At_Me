package text.foryou.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import text.foryou.data.model.FontColorEntity
import text.foryou.data.model.FontStyleEntity

@Dao
interface FontStyleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fontStyle: FontStyleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(fontStyle: List<FontStyleEntity>)

    //LiveData is kind of observer, so the data is updated automatically
    @Query("SELECT * FROM fontstyle ORDER BY id ASC")
    fun getAll(): LiveData<List<FontStyleEntity>>

    @Query("SELECT * FROM fontstyle WHERE id = :id")
    fun getfontStyleById(id: Int): FontStyleEntity? //it is nullable

    @Query("SELECT COUNT(*) from fontstyle")
    fun getCount(): Int

    @Query("SELECT * FROM fontstyle where style = :style")
    fun getfontStyleByStyle(style: String): FontStyleEntity?

    @Query("SELECT id FROM fontstyle where style = :style")
    fun getfontIdByStyle(style: String): Int

}