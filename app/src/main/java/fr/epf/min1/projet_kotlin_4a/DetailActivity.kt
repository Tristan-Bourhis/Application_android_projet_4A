package fr.epf.min1.projet_kotlin_4a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //verifier si la station est en favoris pour init l'item du menu ou changer item par button

        val station_id = intent.getIntExtra("station_id", -1)
        val station_idTextView = findViewById<TextView>(R.id.station_idTextView)
        station_idTextView.text = station_id.toString()

        val nameTextView = findViewById<TextView>(R.id.detailNameTextView)
        nameTextView.text = intent.getStringExtra("name")

        val capacity = intent.getIntExtra("capacity", -1)
        val capacityTextView = findViewById<TextView>(R.id.capacityTextView)
        capacityTextView.text = capacity.toString()

        val nbVelo = intent.getIntExtra("nbVelo", -1)
        val ebike = intent.getIntExtra("ebike", -1)
        val mecanique = nbVelo - ebike

        val nbVeloMecaniqueTextView = findViewById<TextView>(R.id.nbVeloMecaniqueTextView)
        nbVeloMecaniqueTextView.text = mecanique.toString()

        val nbEbikeTextView = findViewById<TextView>(R.id.nbEbikeTextView)
        nbEbikeTextView.text = ebike.toString()

        val nbPlace = capacity - nbVelo
        val nbPlaceTextView = findViewById<TextView>(R.id.nbPlaceTextView)
        nbPlaceTextView.text = nbPlace.toString()
    }
}