package fr.epf.min1.projet_kotlin_4a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import fr.epf.min1.projet_kotlin_4a.bdd.AppDataBase
import fr.epf.min1.projet_kotlin_4a.bdd.StationEntity
import kotlinx.coroutines.runBlocking

class DetailActivity : AppCompatActivity() {

    private var favorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val idStation = intent.getIntExtra("station_id", -1)
        val station_idTextView = findViewById<TextView>(R.id.station_idTextView)
        station_idTextView.text = idStation.toString()

        val name = intent.getStringExtra("name")
        val nameTextView = findViewById<TextView>(R.id.detailNameTextView)
        nameTextView.text = name

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

        val favoriteImageButton = findViewById<ImageButton>(R.id.favoriteImageButton)

        val bdd = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "stationEntity"
        ).build()

        val stationDao = bdd.stationDao()
        runBlocking {
            val listeStation = stationDao.getAll()
            for(i in listeStation){
                if(i.idStation == idStation){
                    favoriteImageButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                    favorite = true
                }
            }
        }


        favoriteImageButton.setOnClickListener {
            val bdd = Room.databaseBuilder(
                applicationContext,
                AppDataBase::class.java, "stationEntity"
            ).build()
            val stationDao = bdd.stationDao()
            val station = StationEntity(
                idStation,
                name!!,
                capacity,
                intent.getStringExtra("stationCode")!!,
                nbVelo,
                ebike
            )
            runBlocking {
                if (favorite) {
                    favoriteImageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    Toast.makeText(applicationContext, "Supprimé aux favoris", Toast.LENGTH_SHORT).show()
                    stationDao.delete(station)
                }else {
                    favoriteImageButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                    Toast.makeText(applicationContext, "Ajouté aux favoris", Toast.LENGTH_SHORT).show()
                    stationDao.insert(station)
                }
            }
        }
    }
}