package com.example.lookatme

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lookatme.data.AppDatabase
import com.example.lookatme.data.DefaultSetProvider
import com.example.lookatme.data.NoteEntity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
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
                val defaultSets = DefaultSetProvider.getSettings()
                val countNote = database?.noteDao()?.getCount()
                val countNum = database?.setDao()?.getCount()
                val defaultFonts = DefaultSetProvider.getFontColor()
                val countNumFonts = database?.fontDao()?.getCount()
                val defaultNotes = DefaultSetProvider.getNotes()

                if(countNote == 0) {
                    database?.noteDao()?.insertAll(defaultNotes)
                }

                if(countNum == 0) {
                database?.setDao()?.insertAll(defaultSets) }

                if(countNumFonts == 0) {
                    database?.fontDao()?.insertAll(defaultFonts)
                }
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

        val rewardAd:RewardedAd = RewardedAd(getApplication(), "ca-app-pub-3940256099942544/5224354917")

        return rewardAd
    }

    fun getContext(): Context {
        return getApplication()
    }

    fun setToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
    }



}