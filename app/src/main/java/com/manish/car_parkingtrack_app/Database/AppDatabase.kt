package com.manish.car_parkingtrack_app.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manish.car_parkingtrack_app.Database.dao.CargoDao
import com.manish.car_parkingtrack_app.Database.dao.CarparkDao
import com.manish.car_parkingtrack_app.Database.entity.Cargo
import com.manish.car_parkingtrack_app.Database.entity.Carpark
@Database(
    entities =[Carpark::class, Cargo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cargoDao(): CargoDao
    abstract fun carparkDao(): CarparkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "TOM_DATABASE"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .setJournalMode(JournalMode.TRUNCATE)
                .build()
        }
    }
}