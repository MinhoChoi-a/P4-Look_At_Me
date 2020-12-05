package com.example.lookatme

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lookatme.data.AppDatabase
import com.example.lookatme.data.DefaultSetProvider
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

    fun addDefaultSet() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val defaultSets = DefaultSetProvider.getSettings()
                val countNum = database?.setDao()?.getCount()

                if(countNum == 0) {
                database?.setDao()?.insertAll(defaultSets) }
            }
        }
    }

    fun checkSize() {

        var size:Int =  noteList?.value?.size ?: 0
        Log.i("notelist_size", size.toString())
    }

}