package fr.epf.min1.projet_kotlin_4a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val nameTextView = findViewById<TextView>(R.id.detailNameTextView)
        nameTextView.text = intent.getStringExtra("name")

        val capacity = intent.getIntExtra("capacity", -1)
        val capacityTextView = findViewById<TextView>(R.id.capacityTextView)
        capacityTextView.text = capacity.toString()

        val nbVelo = intent.getIntExtra("nbVelo", -1)
        val nbVeloMecaniqueTextView = findViewById<TextView>(R.id.nbVeloMecaniqueTextView)
        nbVeloMecaniqueTextView.text = nbVelo.toString()

        val nbPlace = capacity - nbVelo
        val nbPlaceTextView = findViewById<TextView>(R.id.nbPlaceTextView)
        nbPlaceTextView.text = nbPlace.toString()
    }
}