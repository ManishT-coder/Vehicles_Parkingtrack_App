package com.manish.car_parkingtrack_app.Database.service

import android.content.Context
import com.manish.car_parkingtrack_app.Database.AppDatabase
import com.manish.car_parkingtrack_app.Database.entity.Carpark

class CarparkService(context: Context){

    private val dao = AppDatabase.getInstance(context).carparkDao()

    suspend fun saveOrUpdate(carpark: Carpark): Long {
        return dao.saveOrUpdate(carpark)
    }

    suspend fun getConfig(): Carpark? {
        return dao.getFirstUser()
    }

    suspend fun getAllUser(): List<Carpark> {
        return dao.getAllUsers()
    }

    suspend fun getUser(phoneno: String): Carpark? {
        return dao.getUserByPhoneNo(phoneno)
    }
    suspend fun getuserByname(owner: String): Carpark{
        return dao.getuserbyName(owner)
    }
}