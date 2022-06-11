package fr.epf.min1.projet_kotlin_4a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.JsonObject
import fr.epf.min1.projet_kotlin_4a.api.StationsService
import fr.epf.min1.projet_kotlin_4a.bdd.AppDataBase
import fr.epf.min1.projet_kotlin_4a.bdd.StationEntity
import fr.epf.min1.projet_kotlin_4a.model.Station
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StationFavoritesActivity : AppCompatActivity() {

    private var stationAdapter: StationAdapter? = null
    private var donneeApi: MutableList<Station> = mutableListOf()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.station_favorites, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh_favorite -> {
                finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}