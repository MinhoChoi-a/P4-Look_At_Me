package text.foryou.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import text.foryou.data.AppDatabase
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

//ViewModel: bind and control data
class MainViewModel(app: Application) : AndroidViewModel(app) {

    //load database
    private val database = AppDatabase.getInstance(app)

    //get list of notes
    val noteList = database?.noteDao()?.getAll()

    fun requestAd():AdRequest {
        MobileAds.initialize(getApplication())
        return AdRequest.Builder().build()
    }
}