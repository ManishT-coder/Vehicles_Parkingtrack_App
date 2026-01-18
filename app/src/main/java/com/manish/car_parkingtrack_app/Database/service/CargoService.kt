package com.manish.car_parkingtrack_app.Database.service

import android.content.Context
import com.manish.car_parkingtrack_app.Database.AppDatabase
import com.manish.car_parkingtrack_app.Database.entity.Carpark

class CargoService(context: Context) {

    private val dao = AppDatabase.getInstance(context).cargoDao()
    private val db = AppDatabase.getInstance(context)
    private val cargoDao = db.cargoDao()
    suspend fun getUser(phone: String, carNum: String): Carpark? {
        return cargoDao.getUser(phone, carNum)
    }

    suspend fun getAllCarparkUsers(): List<Carpark> {
        return dao.getAllCarparkUsers()
    }
}