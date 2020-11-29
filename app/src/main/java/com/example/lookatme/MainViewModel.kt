package com.example.lookatme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lookatme.data.AppDatabase
import com.example.lookatme.data.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)

    val noteList = database?.noteDao()?.getAll()

    fun deleteNotes(selectedNotes: ArrayList<NoteEntity>) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.noteDao()?.deleteNotes(selectedNotes)
            }
        }
    }

}