package com.manish.car_parkingtrack_app.Model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.manish.car_parkingtrack_app.Database.entity.Carpark
import com.manish.car_parkingtrack_app.Database.service.CargoService

class CargoModel(
    private val cargoService: CargoService
) : ViewModel() {

    private val RATE_PER_TEN_MINS = 10 // 10 Rupees

    var foundCar by mutableStateOf<Carpark?>(null)
    // UI State
    var carnumber by mutableStateOf("")
    var phoneno by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    // Error State
    var carnumbererror by mutableStateOf<String?>(null)
    var phonenoerror by mutableStateOf<String?>(null)
    var getcarerror by mutableStateOf<String?>(null)

    fun isValid(): Boolean {
        var valid = true

        // Car Number Check
        if (carnumber.isBlank()) {
            carnumbererror = "Car number is required"
            valid = false
        } else {
            carnumbererror = null
        }

        // Phone Number Regex Check (Exactly 10 digits)
        val phoneRegex = "^[0-9]{10}$".toRegex()
        if (!phoneno.matches(phoneRegex)) {
            phonenoerror = "Must be exactly 10 digits"
            valid = false
        } else {
            phonenoerror = null
        }

        return valid
    }

    suspend fun checkcar(): Boolean {
        if (!isValid()) return false

        isLoading = true
        return try {
            val result = cargoService.getUser(phoneno, carnumber)
            if (result != null) {
                foundCar = result // <--- SAVE the car details here
                getcarerror = null
                calculateFare()
                true
            } else {
                foundCar = null
                getcarerror = "Car not found"
                false
            }
        } catch (e: Exception) {
            getcarerror = "DB Error: ${e.localizedMessage}"
            false
        } finally {
            isLoading = false
        }
    }
    // Optional: Update this to see minutes clearly during testing
    fun getParkingDuration(): String {
        val entry = foundCar?.EntryTime ?: return ""
        val now = java.time.LocalDateTime.now()

        val duration = java.time.Duration.between(entry, now)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60 // Added seconds for testing

        return if (hours > 0) "$hours hrs $minutes mins" else "$minutes mins $seconds secs"
    }
    var totalFare by mutableStateOf(0) // To store the calculated cost



    fun calculateFare() {
        val entry = foundCar?.EntryTime ?: return
        val now = java.time.LocalDateTime.now()

        val duration = java.time.Duration.between(entry, now)
        val totalMinutes = duration.toMinutes()

        // LOGIC: Every 10-minute block costs 10 rupees.
        // We use (totalMinutes + 9) / 10 to always round up.
        // Example:
        // 1 min  -> (1+9)/10  = 1 block  -> 10 Rupees
        // 10 min -> (10+9)/10 = 1 block  -> 10 Rupees
        // 11 min -> (11+9)/10 = 2 blocks -> 20 Rupees

        val blocks = if (totalMinutes < 1) 1 else (totalMinutes + 9) / 10

        totalFare = (blocks * RATE_PER_TEN_MINS).toInt()
    }

}