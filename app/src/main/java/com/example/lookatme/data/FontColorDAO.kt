package com.example.lookatme.data

import androidx.lifecycle.LiveData
import androidx.room.*

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
    fun getfontById(id: Int): FontColorEntity? //it is nullable

    @Query("SELECT COUNT(*) from fontscolor")
    fun getCount(): Int

}