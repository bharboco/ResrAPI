package iroma.app.restapi

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        GlobalScope.launch { Log.d("Dota",CyberScoreApi.retrofitService.loadHeroes().map{"${it.localized_name} ${it.attack_type}"}.toString())  }

    }
}