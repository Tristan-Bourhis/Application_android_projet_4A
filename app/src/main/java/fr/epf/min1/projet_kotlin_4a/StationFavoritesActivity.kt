package fr.epf.min1.projet_kotlin_4a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import fr.epf.min1.projet_kotlin_4a.bdd.AppDataBase
import kotlinx.coroutines.runBlocking

class StationFavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_favorites)

        val bdd = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "stationEntity"
        ).build()

        val stationDao = bdd.stationDao()
        runBlocking {
            val listStationsFavorites = stationDao.getAll()
        }

    }
}