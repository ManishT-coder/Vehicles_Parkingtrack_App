package com.manish.car_parkingtrack_app.Database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity("Carpark")
data class Carpark(

    @PrimaryKey(autoGenerate = true) val id: Int =0,
            @SerializedName("OwerName") @ColumnInfo(name = "OwerName") val OwerName : String,
    @SerializedName("CarNumber") @ColumnInfo(name = "CarNumber") val CarNumber : String,
    @SerializedName("PhoneNO") @ColumnInfo(name = "PhoneNO") val PhoneNO : String,
    @SerializedName("EntryTime",) @ColumnInfo(name = "EntryTime") val EntryTime : LocalDateTime,
)
