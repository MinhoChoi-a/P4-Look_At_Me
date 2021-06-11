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

    //load database
    private val database = AppDatabase.getInstance(app)

    //find the current note
    val currentNote = MutableLiveData<NoteEntity>()

    private lateinit var mInterestAd: InterstitialAd

    //find note data by id
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

    //to use the resource file of the application
    fun getContext(): Context {
        return getApplication()
    }

    //load interstitial Ad
    fun loadInterAd(): InterstitialAd {
        return InterstitialAd(getApplication())
    }


}