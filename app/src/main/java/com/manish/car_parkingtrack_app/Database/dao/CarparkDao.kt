package com.manish.car_parkingtrack_app.Database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manish.car_parkingtrack_app.Database.entity.Carpark

@Dao
interface CarparkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(carpark: Carpark): Long

    // --- FLEXIBLE SEARCH (Senior's Suggestion) ---
    // This allows searching using ONE string that checks both columns
    // REQUIREMENT 1: Search by either Phone OR Car Number
    @Query("SELECT * FROM Carpark WHERE PhoneNO = :query OR CarNumber = :query LIMIT 1")
    suspend fun searchVehicle(query: String): Carpark?

    // REQUIREMENT 2: Dashboard Statistics
    @Query("SELECT COUNT(*) FROM Carpark")
    suspend fun getTotalVehicles(): Int

    // --- DASHBOARD DATA (Senior's Suggestion) ---
    @Query("SELECT COUNT(*) FROM Carpark")
    suspend fun getTotalVehicleCount(): Int

    @Query("SELECT * FROM Carpark")
    suspend fun getAllEntries(): List<Carpark>

    // --- OTHER UTILITIES ---
    @Query("SELECT * FROM Carpark WHERE id = :id")
    suspend fun getUserById(id: Int): Carpark?

    @Query("SELECT COUNT(*) FROM Carpark")
    suspend fun getTotalCars(): Int

    @Query("DELETE FROM Carpark")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteUser(carpark: Carpark)
}