package text.foryou

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import text.foryou.data.AppDatabase
import text.foryou.data.DefaultSetProvider
import text.foryou.data.NoteEntity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AppDatabase.getInstance(app)

    val noteList = database?.noteDao()?.getAll()

    private lateinit var mInterestAd: InterstitialAd

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

                val countNote = database?.noteDao()?.getCount()
                val countNum = database?.setDao()?.getCount()
                val countNumFonts = database?.fontColorDao()?.getCount()
                val countNumFontStyle = database?.fontStyleDao()?.getCount()

                val defaultSets = DefaultSetProvider.getSettings()
                val defaultFontStyle = DefaultSetProvider.getFontStyle()
                val defaultFonts = DefaultSetProvider.getFontColor()
                val defaultNotes = DefaultSetProvider.getNotes()

                if(countNote == 0) {
                    database?.noteDao()?.insertAll(defaultNotes) }

                if(countNum == 0) {
                database?.setDao()?.insertAll(defaultSets) }

                if(countNumFonts == 0) {
                    database?.fontColorDao()?.insertAll(defaultFonts) }

                if(countNumFontStyle == 0) {
                    database?.fontStyleDao()?.insertAll(defaultFontStyle) }
            }
        }
    }

    fun checkSize():Int {
        var noteSize:Int =  noteList?.value?.size ?: 0
        return noteSize
    }

    fun requestAd():AdRequest {
        MobileAds.initialize(getApplication())
        return AdRequest.Builder().build()
    }

    fun loadRewardAd(): RewardedAd {
        return RewardedAd(getApplication(), "")
    }

    fun loadInterAd(): InterstitialAd {
        return InterstitialAd(getApplication())
    }

    fun getContext(): Context {
        return getApplication()
    }

    fun setToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
    }



}