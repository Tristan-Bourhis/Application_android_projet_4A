package fr.epf.min1.projet_kotlin_4a.bdd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StationDAO {
    @Query("SELECT * FROM stationEntity")
    suspend fun getAll(): List<StationEntity>

    @Insert
    suspend fun insert(vararg station: StationEntity)

    @Delete
    suspend fun delete(station: StationEntity)
}