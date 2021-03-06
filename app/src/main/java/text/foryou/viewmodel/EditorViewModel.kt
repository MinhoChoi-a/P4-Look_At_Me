package text.foryou.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import text.foryou.data.AppDatabase
import text.foryou.data.model.NoteEntity
import text.foryou.data.model.BackgroundEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import text.foryou.NEW_NOTE_ID
import text.foryou.data.model.FontColorEntity
import text.foryou.data.model.FontStyleEntity

class EditorViewModel(app: Application): AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)

    var setList = database?.setDao()?.getAll()
    var fontList = database?.fontColorDao()?.getAll()
    var fontStyleList = database?.fontStyleDao()?.getAll()

    //MutableLiveData: this can react on OnChanged event
    val currentNote = MutableLiveData<NoteEntity>()
    val selectedFontColor = MutableLiveData<FontColorEntity>()
    val selectedBackground = MutableLiveData<BackgroundEntity>()
    val selectedFontStyle = MutableLiveData<FontStyleEntity>()

    //get note data with the ID
    fun getNoteById(noteId: Int) {

        //using coroutine scope, this process will be working on this ViewModel(Editor) only
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val note =
                        if(noteId != NEW_NOTE_ID) {
                            database?.noteDao()?.getNoteById(noteId)
                        }
                        else{
                            NoteEntity()
                        }
                currentNote.postValue(note) //store value using thread. This is available on MutableLiveData
            }
        }
    }

    //update the note data(mutableLiveData) and database
    fun updateNote() {

        //let: invoke one or more functions on results of all chains
        currentNote.value?.let {

            it.text = it.text.trim()

            //if data is empty, then do nothing
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

    //delete note
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.noteDao()?.deleteNote(note)
            }
        }
    }

    //set and display the message of toast
    fun setToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
    }

    //find whole data for the Note entity
    fun findSetAndAddToNote(setId: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val background: BackgroundEntity? = database?.setDao()?.getSetById(setId)
                currentNote.value?.backRes = background?.res!!.toString()
                currentNote.value?.backType = background?.type!!.toInt()
                updateNote()
            }
        }
    }

    //find the position number of the stored font color
    fun getSelectedFontColorPosition() {

        val color = currentNote.value?.fontColor
        //Log.i("color_check", color.toString())
        if(color != null) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val fc = database?.fontColorDao()?.getfontByColor(color)
                selectedFontColor.postValue(fc)
                }
            }
        }
    }

    //return the position of the selected font color
    fun returnFontColorPosition(): Int {

        if(selectedFontColor.value?.id != null) {
            return selectedFontColor.value?.id!!
        }
        return 0

    }

    //find the position number of the stored background
    fun getSelectedBackPosition() {

        val res = currentNote.value?.backRes
        if(res != null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val rs = database?.setDao()?.getSetByRes(res)
                    selectedBackground.postValue(rs)
                }
            }
        }
    }

    //return the position of the selected background
    fun returnBackPosition(): Int {

        if(selectedBackground.value?.id != null) {
            return selectedBackground.value?.id!!
        }
        return 0

    }

    //find the position number of the stored font style
    fun getSelectedFontStylePosition() {

        val style = currentNote.value?.fontStyle

        if(style != null) {

            viewModelScope.launch {
                withContext(Dispatchers.IO) {

                    val rs = database?.fontStyleDao()?.getfontStyleByStyle(style)
                    selectedFontStyle.postValue(rs)
                }
            }
        }
    }

    //return the position of the selected font style
    fun returnFontStylePosition(): Int {

        if(selectedFontStyle.value?.id != null) {
            return selectedFontStyle.value?.id!!
        }
        return 0
    }

    //request ad
    fun requestAd(): AdRequest {
        MobileAds.initialize(getApplication())
        return AdRequest.Builder().build()
    }
}