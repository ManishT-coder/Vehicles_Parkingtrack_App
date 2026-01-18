package com.manish.car_parkingtrack_app.Model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.manish.car_parkingtrack_app.Database.entity.Carpark
import com.manish.car_parkingtrack_app.Database.service.CarparkService
import java.time.LocalDateTime

class CarparkModel(
    private val carparkService: CarparkService
): ViewModel() {
    var carnumber by mutableStateOf("")
    var phoneno by mutableStateOf("")
    var OwerName by mutableStateOf("")

    var carnumbererror by mutableStateOf<String?>(null)
    var phonenoerror by mutableStateOf<String?>(null)
    var Owernameerror by mutableStateOf<String?>(null)
    var parkerror by mutableStateOf<String?>(null)

    fun isValid(): Boolean{
        var valid=true
        if(carnumber.isBlank()){
            carnumbererror="Please enter your car number"
            valid=false
        }else{
            carnumbererror=null
        }
        if(phoneno.isBlank()){
            phonenoerror="Please enter your phone number"
            valid=false
        }else{
            phonenoerror=null
        }
        if (OwerName.isBlank()){
            Owernameerror="Please enter you name "
            valid=false
        }else{
            Owernameerror=null
        }
        return valid
    }

    fun clearFields() {
        OwerName=""
        phoneno=""
        carnumber=""

        Owernameerror = null
        phonenoerror = null
        carnumbererror = null
    }
    suspend fun parking(): Boolean {
        if (!isValid()) return false

        val result = carparkService.saveOrUpdate(
            Carpark (
                id=0,
                OwerName=OwerName,
                PhoneNO=phoneno,
                CarNumber=carnumber,
                EntryTime = LocalDateTime.now(),
            )
        )

        return if (result>0) {
            parkerror = null
            true
        } else {
            parkerror = "Parking  failed"
            false
        }
    }
}