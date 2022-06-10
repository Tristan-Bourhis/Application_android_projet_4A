package fr.epf.min1.projet_kotlin_4a.bdd

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StationEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun stationDao() : StationDAO
}