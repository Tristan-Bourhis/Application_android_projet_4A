package fr.epf.min1.projet_kotlin_4a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import fr.epf.min1.projet_kotlin_4a.bdd.AppDataBase
import kotlinx.coroutines.runBlocking

class StationFavoritesActivity : AppCompatActivity() {

    private var stationAdapter: StationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_favorites)

        val recyclerView = findViewById<RecyclerView>(R.id.list_favorites_stations)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val bdd = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "stationEntity"
        ).build()

        val stationDao = bdd.stationDao()
        runBlocking {
            val listeStationsFavorites = stationDao.getAll()
            stationAdapter = StationAdapter(listeStationsFavorites)
            recyclerView.adapter = StationAdapter(listeStationsFavorites)
        }
    }

}