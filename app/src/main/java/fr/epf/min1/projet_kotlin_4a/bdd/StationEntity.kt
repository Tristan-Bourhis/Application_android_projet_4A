package fr.epf.min1.projet_kotlin_4a.bdd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StationEntity (
    @PrimaryKey val idStation: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "capacity") val capacity: Int,
    @ColumnInfo(name = "stationCode") val stationCode: String,
    @ColumnInfo(name = "nbVelo") val nbVelo: Int,
    @ColumnInfo(name = "ebike") val ebike: Int
)

