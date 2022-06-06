package fr.epf.min1.projet_kotlin_4a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class StationFavoritesActivity : AppCompatActivity() {

    lateinit var tvResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_favorites)

        tvResponse = findViewById(R.id.reponce)

    }
}