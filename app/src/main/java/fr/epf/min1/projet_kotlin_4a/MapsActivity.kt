package fr.epf.min1.projet_kotlin_4a

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var listeStation: MutableList<Station> = mutableListOf()
    private var swap: Boolean = false
    private lateinit var client: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private var REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        client = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        swap = intent.getBooleanExtra("swap", false)
        api()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.station_favorite_home_button -> {
                startActivity(Intent(this, StationFavoritesActivity::class.java))
            }
            R.id.refresh_home_button -> {
                finish()
                startActivity(intent)
            }
            R.id.swap_home_button -> {
                finish()
                val intent = Intent(this, MapsActivity::class.java)
                if (swap){
                    intent.putExtra("swap", false)
                }else {
                    intent.putExtra("swap", true)
                }
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener(this)
/*
        // Add a marker in Sydney and move the camera
        val paris = LatLng(48.8, 2.3)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 16.0f))*/
    }

    override fun onInfoWindowClick(marker: Marker) {
        val intent = Intent(this, DetailActivity::class.java)
        for (i in listeStation) {
            val positionStation = LatLng(i.lat, i.lon)
            if (marker.position == positionStation) {
                intent.putExtra("station_id", i.station_id)
                intent.putExtra("name", i.name)
                intent.putExtra("capacity", i.capacity)
                intent.putExtra("nbVelo", i.nbVelo)
                intent.putExtra("ebike", i.ebike)
                intent.putExtra("stationCode", i.stationCode)
            }
        }
        startActivity(intent)
    }

    private fun api() {
        //create retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://velib-metropole-opendata.smoove.pro/opendata/Velib_Metropole/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val stationService = retrofit.create(StationsService::class.java)
        val statusService = retrofit.create(StationsService::class.java)

        //call api
        val resultStatus = statusService.getStatus()
        val result = stationService.getStations()
        result.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    Toast.makeText(applicationContext, "Chargement des données", Toast.LENGTH_SHORT).show()
                    val result = response.body()
                    val data = result?.get("data")?.asJsonObject
                    val station = data?.get("stations")?.asJsonArray
                    if (station != null) {
                        resultStatus.enqueue(object : Callback<JsonObject>{
                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                if (response.isSuccessful) {
                                    val resultStatus = response.body()
                                    val dataStatus = resultStatus?.get("data")?.asJsonObject
                                    val stationStatus = dataStatus?.get("stations")?.asJsonArray
                                    for (i in station) {
                                        val stationCode = i.asJsonObject.get("stationCode").asString
                                        var nbVelo = 0
                                        var ebike = 0
                                        if (stationStatus != null) {
                                            for (j in stationStatus) {
                                                if (stationCode == j.asJsonObject.get("stationCode").asString) {
                                                    nbVelo = j.asJsonObject.get("numBikesAvailable").asInt
                                                    val type = j.asJsonObject.get("num_bikes_available_types").asJsonArray
                                                    val typeElectrique = type.get(1).asJsonObject
                                                    ebike = typeElectrique.get("ebike").asInt
                                                }
                                            }
                                        }
                                        val capacity = i.asJsonObject.get("capacity").asInt
                                        val nouvelleStation = Station(
                                            i.asJsonObject.get("station_id").asInt,
                                            i.asJsonObject.get("name").asString,
                                            i.asJsonObject.get("lat").asDouble,
                                            i.asJsonObject.get("lon").asDouble,
                                            capacity,
                                            stationCode,
                                            nbVelo,
                                            ebike
                                        )
                                        listeStation.add(nouvelleStation)
                                        val position =
                                            LatLng(nouvelleStation.lat, nouvelleStation.lon)
                                        val nbPlaceDisponible = capacity - nbVelo
                                        if(nbVelo!=0 && !swap) {
                                            mMap.addMarker(
                                                MarkerOptions()
                                                    .position(position)
                                                    .title(nouvelleStation.name)
                                                    .icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_GREEN
                                                        )
                                                    )
                                                    .snippet("${nouvelleStation.nbVelo} vélo(s) disponible(s)")
                                            )
                                        }else if (nbVelo==0 && !swap){
                                            mMap.addMarker(
                                                MarkerOptions()
                                                    .position(position)
                                                    .title(nouvelleStation.name)
                                                    .icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_RED
                                                        )
                                                    )
                                                    .snippet("${nouvelleStation.nbVelo} vélo(s) disponible(s)")
                                            )
                                        }else if (nbPlaceDisponible!=0 && swap) {
                                            mMap.addMarker(
                                                MarkerOptions()
                                                    .position(position)
                                                    .title(nouvelleStation.name)
                                                    .icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_GREEN
                                                        )
                                                    )
                                                    .snippet("${nbPlaceDisponible} place(s) disponible(s)")
                                            )
                                        }else {
                                            mMap.addMarker(
                                                MarkerOptions()
                                                    .position(position)
                                                    .title(nouvelleStation.name)
                                                    .icon(
                                                        BitmapDescriptorFactory.defaultMarker(
                                                            BitmapDescriptorFactory.HUE_RED
                                                        )
                                                    )
                                                    .snippet("0 place disponible")
                                            )
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task: Task<Location> = client.getLastLocation()
        task.addOnSuccessListener(OnSuccessListener<Any?> { location ->
            if (location != null) {
                currentLocation = location as Location
                val position = LatLng(currentLocation.latitude, currentLocation.longitude)
                mMap.addMarker(MarkerOptions().position(position).title("Ma position").icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE
                    )
                ))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0f))
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }
}

