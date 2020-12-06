package com.example.lookatme

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lookatme.data.AppDatabase
import com.example.lookatme.data.DefaultSetProvider
import com.example.lookatme.data.NoteEntity
import com.example.lookatme.data.SetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(app: Application): AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val currentNote = MutableLiveData<NoteEntity>()
    var setList = database?.setDao()?.getAll()
    var fontList = database?.fontDao()?.getAll()

    fun getNoteById(noteId: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val note =
                        if(noteId != NEW_NOTE_ID) {
                            database?.noteDao()?.getNoteById(noteId)
                        }
                else{
                            NoteEntity()
                        }
                currentNote.postValue(note)
            }
        }
    }

    fun updateNote() {
        currentNote.value?.let {
            it.text = it.text.trim()

            if(it.id == NEW_NOTE_ID && it.text.isEmpty()) {
                return
            }

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if(it.text.isEmpty()) {
                        database?.noteDao()?.deleteNote(it)
                    }
                    else {
                        database?.noteDao()?.insertNote(it)
                    }
                }
            }
        }
    }

    fun deleteNote(note: NoteEntity) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.noteDao()?.deleteNote(note)
            }
        }
    }

    fun findSetAndAddToNote(setId: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val set: SetEntity? = database?.setDao()?.getSetById(setId)

                Log.i("findset in viewmodel", set?.res.toString())

                currentNote.value?.backRes = set?.res!!.toString()
                currentNote.value?.backType = set?.type!!.toInt()

                updateNote()



            }
        }
    }
}