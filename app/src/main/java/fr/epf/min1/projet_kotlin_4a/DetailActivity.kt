package fr.epf.min1.projet_kotlin_4a

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //verifier si la station est en favoris pour init l'item du menu

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_detail_button -> {
                //TODO
                //fill heart
                //add to favorite
            }
        }
        return super.onOptionsItemSelected(item)
    }
}