package text.foryou

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import text.foryou.data.AppDatabase
import text.foryou.data.NoteEntity
import text.foryou.data.BackgroundEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import text.foryou.data.FontColorEntity

class EditorViewModel(app: Application): AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val currentNote = MutableLiveData<NoteEntity>()
    var setList = database?.setDao()?.getAll()
    var fontList = database?.fontColorDao()?.getAll()
    var fontStyleList = database?.fontStyleDao()?.getAll()

    val fontColor = MutableLiveData<FontColorEntity>()

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

                currentNote.value?.backRes = background?.res!!.toString()
                currentNote.value?.backType = background?.type!!.toInt()

                updateNote()
            }
        }
    }

    fun requestAd(): AdRequest {
        MobileAds.initialize(getApplication())
        return AdRequest.Builder().build()
    }

    fun getSelectedFontColorPosition() {

        val color = currentNote.value?.fontColor

        Log.i("color_check", color.toString())

        if(color != null) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val fc = database?.fontColorDao()?.getfontByColor(color)
                fontColor.postValue(fc)

                Log.i("firstcheck", "check")
                }
            }
        }
    }

    fun returnFontColorPosition(): Int {

        Log.i("lastcheck", "check")

        if(fontColor.value?.id != null) {
            return fontColor.value?.id!!
        }

        return 0

    }
}