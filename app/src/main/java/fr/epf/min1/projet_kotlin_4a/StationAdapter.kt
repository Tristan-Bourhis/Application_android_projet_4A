package fr.epf.min1.projet_kotlin_4a

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.epf.min1.projet_kotlin_4a.bdd.StationEntity

class StationAdapter (val listeStation: List<StationEntity>) : RecyclerView.Adapter<StationAdapter.StationViewHolder>(){

    class StationViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val stationView = inflater.inflate(R.layout.adapter_station, parent, false)
        return StationViewHolder(stationView)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val stationEntity = listeStation[position]

        holder.view.setOnClickListener{
            val context = it.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("station_id", stationEntity.idStation)
            intent.putExtra("name", stationEntity.name)
            intent.putExtra("capacity", stationEntity.capacity)
            intent.putExtra("nbVelo", stationEntity.nbVelo)
            intent.putExtra("ebike", stationEntity.ebike)
            intent.putExtra("stationCode", stationEntity.stationCode)
            context.startActivity(intent)
        }

        val stationNameTV = holder.view.findViewById<TextView>(R.id.adapter_station_name)
        stationNameTV.text = stationEntity.name

        val stationCapacityTV = holder.view.findViewById<TextView>(R.id.adapter_station_capacity)
        stationCapacityTV.text = "Vélos disponibles : " + stationEntity.nbVelo
    }

    override fun getItemCount(): Int = listeStation.size
}