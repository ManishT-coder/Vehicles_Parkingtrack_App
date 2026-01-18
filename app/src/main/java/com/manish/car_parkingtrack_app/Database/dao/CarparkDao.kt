package com.manish.car_parkingtrack_app.Database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manish.car_parkingtrack_app.Database.entity.Carpark
import java.security.acl.Owner


@Dao
interface CarparkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(carpark: Carpark)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(carpark: List<Carpark>)

    @Query("SELECT * FROM Carpark LIMIT 1")
    suspend fun getFirstUser(): Carpark?

    @Query("SELECT * FROM Carpark")
    suspend fun getAllUsers(): List<Carpark>

    @Query("SELECT * FROM Carpark WHERE PhoneNO = :Phoneno")
    suspend fun getUserByPhoneNo(Phoneno: String): Carpark?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(carpark: Carpark): Long

    @Query("DELETE FROM Carpark")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteUser(carpark: Carpark)

    @Query("SELECT COUNT(*) FROM Carpark")
    suspend fun getAllUserCount(): Long

    @Query("SELECT * FROM  Carpark WHERE OwerName = :Owner")
    suspend fun getuserbyName(Owner: String): Carpark
}