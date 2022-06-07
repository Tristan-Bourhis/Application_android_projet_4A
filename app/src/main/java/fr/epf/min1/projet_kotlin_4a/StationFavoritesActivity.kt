package fr.epf.min1.projet_kotlin_4a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import fr.epf.min1.projet_kotlin_4a.api.StationsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StationFavoritesActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_favorites)

        var textView: TextView = findViewById(R.id.reponce)
        //textView.setText("3")
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
                            textView.text = station.toString()
                            /*if (16107 ==  i.asJsonObject.get("stationCode").asString) {
                                nbVelo = i.asJsonObject.get("num_bikes_available").asInt
                            }*/
                        }
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur ici", Toast.LENGTH_SHORT).show()
            }
        })
    }
}