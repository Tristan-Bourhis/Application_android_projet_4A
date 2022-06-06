package fr.epf.min1.projet_kotlin_4a.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface StationsService {

    @GET("station_information.json")
    fun getStations() : Call<JsonObject>

    @GET("station_status.json")
    fun getStatus(): Call<JsonObject>
}