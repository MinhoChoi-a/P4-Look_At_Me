package com.example.lookatme

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lookatme.data.AppDatabase
import com.example.lookatme.data.NoteEntity
import com.example.lookatme.data.BackgroundEntity
import com.example.lookatme.data.FontStyleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(app: Application): AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val currentNote = MutableLiveData<NoteEntity>()
    var setList = database?.setDao()?.getAll()
    var fontList = database?.fontColorDao()?.getAll()
    var fontStyleList = database?.fontStyleDao()?.getAll()

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

    fun setToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
    }


    fun findSetAndAddToNote(setId: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val background: BackgroundEntity? = database?.setDao()?.getSetById(setId)

                Log.i("findset in viewmodel", background?.res.toString())

                currentNote.value?.backRes = background?.res!!.toString()
                currentNote.value?.backType = background?.type!!.toInt()

                updateNote()
            }
        }
    }

    fun getContext(): Context {
        return getApplication()
    }
}