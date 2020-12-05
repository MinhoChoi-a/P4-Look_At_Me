package com.example.lookatme.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SetDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSet(set: SetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(notes: List<SetEntity>)

    //LiveData is kind of observer, so the data is updated automatically
    @Query("SELECT * FROM settings ORDER BY id ASC")
    fun getAll(): LiveData<List<SetEntity>>

    @Query("SELECT * FROM settings WHERE id = :id")
    fun getSetById(id: Int): SetEntity? //it is nullable

    @Query("SELECT COUNT(*) FROM settings")
    fun getCount(): Int?
}