package text.foryou

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//Entry point of this application
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.mainTheme)
        setContentView(R.layout.activity_main)
    }
}