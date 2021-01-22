package text.foryou.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.InterstitialAd
import text.foryou.data.AppDatabase
import text.foryou.data.model.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import text.foryou.NEW_NOTE_ID

class DisplayViewModel(app: Application): AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)
    val currentNote = MutableLiveData<NoteEntity>()

    private lateinit var mInterestAd: InterstitialAd

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

    fun getContext(): Context {
        return getApplication()
    }

    fun loadInterAd(): InterstitialAd {
        return InterstitialAd(getApplication())
    }


}