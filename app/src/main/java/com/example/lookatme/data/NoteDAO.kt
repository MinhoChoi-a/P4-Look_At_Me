package com.example.lookatme.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(notes: List<NoteEntity>)

    //LiveData is kind of observer, so the data is updated automatically
    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAll(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): NoteEntity? //it is nullable

    @Query("SELECT COUNT(*) from notes")
    fun getCount(): Int

    @Delete
    fun deleteNotes(selectedNotes: ArrayList<NoteEntity>):Int

    @Query("DELETE FROM notes")
    fun deleteAll()

    @Delete
    fun deleteNote(note: NoteEntity)

}