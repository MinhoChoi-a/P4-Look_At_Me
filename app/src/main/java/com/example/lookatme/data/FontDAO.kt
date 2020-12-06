package com.example.lookatme.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FontDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(font: FontEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(fonts: List<FontEntity>)

    //LiveData is kind of observer, so the data is updated automatically
    @Query("SELECT * FROM fonts ORDER BY id ASC")
    fun getAll(): LiveData<List<FontEntity>>

    @Query("SELECT * FROM fonts WHERE id = :id")
    fun getfontById(id: Int): FontEntity? //it is nullable

    @Query("SELECT COUNT(*) from fonts")
    fun getCount(): Int

}