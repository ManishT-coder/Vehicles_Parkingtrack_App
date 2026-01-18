package com.manish.car_parkingtrack_app.Database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer
import java.time.LocalDateTime

@Entity("Cargo")
data class Cargo(

    @PrimaryKey(autoGenerate = true) val id : Int,
    @SerializedName("PhoneNo")@ColumnInfo("PhoneNO") val PhoneNO: Int,
    @SerializedName("CarNumber")@ColumnInfo("CarNumber") val CarNumber: String
)
