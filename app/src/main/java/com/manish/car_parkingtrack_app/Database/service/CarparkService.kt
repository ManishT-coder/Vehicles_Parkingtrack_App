package com.manish.car_parkingtrack_app.Database.service

import android.content.Context
import com.manish.car_parkingtrack_app.Database.AppDatabase
import com.manish.car_parkingtrack_app.Database.entity.Carpark

class CarparkService(context: Context) {

    private val dao = AppDatabase.getInstance(context).carparkDao()

    suspend fun saveOrUpdate(carpark: Carpark): Long {
        return dao.saveOrUpdate(carpark)
    }

    // This handles the "Phone OR Vehicle Number" search
    suspend fun findVehicle(query: String): Carpark? {
        return dao.searchVehicle(query)
    }

    // This provides the count for your Dashboard
    suspend fun getTotalCount(): Int {
        return dao.getTotalVehicleCount()
    }
    suspend fun getAllEntries(): List<Carpark> {
        return dao.getAllEntries() // Calls the function we added to the Dao
    }

    // This provides all data for advanced analysis
    suspend fun getAllParkingRecords(): List<Carpark> {
        return dao.getAllEntries()
    }

    suspend fun deleteRecord(carpark: Carpark) {
        dao.deleteUser(carpark)
    }
}