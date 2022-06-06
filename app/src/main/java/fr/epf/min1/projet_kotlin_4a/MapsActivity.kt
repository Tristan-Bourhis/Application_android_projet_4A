package fr.epf.min1.projet_kotlin_4a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonObject
import fr.epf.min1.projet_kotlin_4a.api.StationsService
import fr.epf.min1.projet_kotlin_4a.databinding.ActivityMapsBinding
import fr.epf.min1.projet_kotlin_4a.model.Station
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var listeStation: MutableList<Station> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //create retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://velib-metropole-opendata.smoove.pro/opendata/Velib_Metropole/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val stationService = retrofit.create(StationsService::class.java)

        //call api
        val result = stationService.getStations()
        result.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    val result = response.body()
                    val data = result?.get("data")?.asJsonObject
                    val station = data?.get("stations")?.asJsonArray
                    if (station != null) {
                        for (i in station) {
                            val stationCode = i.asJsonObject.get("stationCode").asString
                            val nouvelleStation = Station(
                                i.asJsonObject.get("station_id").asInt,
                                i.asJsonObject.get("name").asString,
                                i.asJsonObject.get("lat").asDouble,
                                i.asJsonObject.get("lon").asDouble,
                                i.asJsonObject.get("capacity").asInt,
                                stationCode,
                                3
                                //getNombreVeloDisponible(stationCode)
                            )
                            listeStation.add(nouvelleStation)
                            val position = LatLng(nouvelleStation.lat, nouvelleStation.lon)
                            mMap.addMarker(MarkerOptions()
                                .position(position)
                                .title(nouvelleStation.name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                //.snippet(  "${nouvelleStation.nbVelo} vélo(s) disponible(s)")
                            )
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(position))
                        }
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getNombreVeloDisponible(stationCode: String): Int {
        var nbVelo = 0
        //create retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://velib-metropole-opendata.smoove.pro/opendata/Velib_Metropole/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val stationService = retrofit.create(StationsService::class.java)

        //call api
        val result = stationService.getStatus()
        result.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    val result = response.body()
                    val data = result?.get("data")?.asJsonObject
                    val station = data?.get("stations")?.asJsonArray
                    if (station != null) {
                        for (i in station) {
                            if (stationCode ==  i.asJsonObject.get("stationCode").asString) {
                                nbVelo = i.asJsonObject.get("num_bikes_available").asInt
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur ici", Toast.LENGTH_SHORT).show()
            }
        })
        return nbVelo;
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.station_favorite_home_button -> {
                startActivity(Intent(this, StationFavoritesActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val paris = LatLng(48.8, 2.3)
        mMap.addMarker(MarkerOptions().position(paris).title("Marker in Paris"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris))
    }
}