package com.manish.car_parkingtrack_app.Database.dao

import androidx.room.Dao
import androidx.room.Query
import com.manish.car_parkingtrack_app.Database.entity.Carpark
@Dao
interface CargoDao {

    @Query("SELECT * FROM Carpark WHERE PhoneNO = :phoneno AND CarNumber = :carnumber LIMIT 1")
    suspend fun authenticate(phoneno: String, carnumber: String): Carpark?

    @Query("SELECT * FROM Carpark")
    suspend fun getAllCarparkUsers(): List<Carpark>

    @Query("SELECT * FROM Carpark WHERE PhoneNO = :phone AND CarNumber = :carNum LIMIT 1")
    suspend fun getUser(phone: String, carNum: String): Carpark?
}