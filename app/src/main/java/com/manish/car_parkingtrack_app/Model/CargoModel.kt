package com.manish.car_parkingtrack_app.Model

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.manish.car_parkingtrack_app.Database.entity.Carpark
import com.manish.car_parkingtrack_app.Database.service.CargoService
import java.time.Duration
import java.time.LocalDateTime

/**
 * 📝 EVOLUTION REMARKS:
 * -------------------------------------------------------------------------
 * PREVIOUS PATTERN (V1):
 * - Used separate 'phoneno' and 'carnumber' inputs.
 * - Forced the user to provide both details to find a record.
 *
 * NEW PROFESSIONAL PATTERN (V2 - Senior Suggestion):
 * - Combined inputs into a single 'searchQuery'.
 * - Added 'foundCars' List to support multiple vehicles per phone number.
 * - Changed 'checkcar' return type to Int to handle 0, 1, or Many results.
 * -------------------------------------------------------------------------
 */

class CargoModel(private val cargoService: CargoService) : ViewModel() {

    // --- [OLD PATTERN VARIABLES - COMMENTED FOR REFERENCE] ---
    /*
    var carnumber by mutableStateOf("")
    var phoneno by mutableStateOf("")

    package com.manish.car_parkingtrack_app.Model

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.manish.car_parkingtrack_app.Database.entity.Carpark
import com.manish.car_parkingtrack_app.Database.service.CargoService
import java.time.Duration
import java.time.LocalDateTime

/**
 * 📝 EVOLUTION REMARKS:
 * -------------------------------------------------------------------------
 * PREVIOUS PATTERN (V1):
 * - Used separate 'phoneno' and 'carnumber' inputs.
 * - Forced the user to provide both details to find a record.
 *
 * NEW PROFESSIONAL PATTERN (V2 - Senior Suggestion):
 * - Combined inputs into a single 'searchQuery'.
 * - Added 'foundCars' List to support multiple vehicles per phone number.
 * - Changed 'checkcar' return type to Int to handle 0, 1, or Many results.
 * -------------------------------------------------------------------------
 */

class CargoModel(private val cargoService: CargoService) : ViewModel() {

    // --- [OLD PATTERN VARIABLES - COMMENTED FOR REFERENCE] ---
    /*
    var carnumber by mutableStateOf("")
    var phoneno by mutableStateOf("")
    */

    // --- [NEW PATTERN VARIABLES] ---
    var searchQuery by mutableStateOf("")
    var foundCars by mutableStateOf<List<Carpark>>(emptyList())
    var foundCar by mutableStateOf<Carpark?>(null)
    var showVehicleSelector by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var getcarerror by mutableStateOf<String?>(null)
    var totalFare by mutableStateOf(0)

    private val FARE_RATE = 10

    // --- [OLD PATTERN LOGIC - COMMENTED FOR REFERENCE] ---
    /*
    suspend fun checkcar_OLD(): Boolean {
        val result = cargoService.getUser(phoneno, carnumber)
        return if (result != null) {
            foundCar = result
            calculateFare()
            true
        } else false
    }
    */

    // --- [NEW PATTERN LOGIC: Flexible Search] ---
    suspend fun checkcar(): Int {
        if (searchQuery.isBlank()) {
            getcarerror = "Input Required (Phone or Plate)"
            return 0
        }

        isLoading = true
        return try {
            val results = cargoService.findVehicles(searchQuery.trim().uppercase())

            when {
                results.isEmpty() -> {
                    getcarerror = "Record not found"
                    0
                }
                results.size == 1 -> {
                    foundCar = results[0]
                    calculateFare()
                    1
                }
                else -> {
                    foundCars = results
                    showVehicleSelector = true // UI will show the list
                    results.size
                }
            }
        } catch (e: Exception) {
            getcarerror = "DB Error: ${e.localizedMessage}"
            0
        } finally {
            isLoading = false
        }
    }

    fun calculateFare() {
        val entry = foundCar?.EntryTime ?: return
        val totalMinutes = Duration.between(entry, LocalDateTime.now()).toMinutes()
        val blocks = if (totalMinutes < 1) 1 else ((totalMinutes + 9) / 10)
        totalFare = (blocks * FARE_RATE).toInt()
    }

    fun getParkingDuration(): String {
        val entry = foundCar?.EntryTime ?: return "--"
        val duration = Duration.between(entry, LocalDateTime.now())
        return "${duration.toHours()}h ${duration.toMinutes() % 60}m"
    }

    fun selectCar(car: Carpark) {
        foundCar = car
        showVehicleSelector = false
        calculateFare()
    }
}


    */

    // --- [NEW PATTERN VARIABLES] ---
    var searchQuery by mutableStateOf("")
    var foundCars by mutableStateOf<List<Carpark>>(emptyList())
    var foundCar by mutableStateOf<Carpark?>(null)
    var showVehicleSelector by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var getcarerror by mutableStateOf<String?>(null)
    var totalFare by mutableStateOf(0)

    private val FARE_RATE = 10

    // --- [OLD PATTERN LOGIC - COMMENTED FOR REFERENCE] ---
    /*
    suspend fun checkcar_OLD(): Boolean {
        val result = cargoService.getUser(phoneno, carnumber)
        return if (result != null) {
            foundCar = result
            calculateFare()
            true
        } else false
    }
    */

    // --- [NEW PATTERN LOGIC: Flexible Search] ---
    suspend fun checkcar(): Int {
        if (searchQuery.isBlank()) {
            getcarerror = "Input Required (Phone or Plate)"
            return 0
        }

        isLoading = true
        return try {
            val results = cargoService.findVehicles(searchQuery.trim().uppercase())

            when {
                results.isEmpty() -> {
                    getcarerror = "Record not found"
                    0
                }
                results.size == 1 -> {
                    foundCar = results[0]
                    calculateFare()
                    1
                }
                else -> {
                    foundCars = results
                    showVehicleSelector = true // UI will show the list
                    results.size
                }
            }
        } catch (e: Exception) {
            getcarerror = "DB Error: ${e.localizedMessage}"
            0
        } finally {
            isLoading = false
        }
    }

    fun calculateFare() {
        val entry = foundCar?.EntryTime ?: return
        val totalMinutes = Duration.between(entry, LocalDateTime.now()).toMinutes()
        val blocks = if (totalMinutes < 1) 1 else ((totalMinutes + 9) / 10)
        totalFare = (blocks * FARE_RATE).toInt()
    }

    fun getParkingDuration(): String {
        val entry = foundCar?.EntryTime ?: return "--"
        val duration = Duration.between(entry, LocalDateTime.now())
        return "${duration.toHours()}h ${duration.toMinutes() % 60}m"
    }

    fun selectCar(car: Carpark) {
        foundCar = car
        showVehicleSelector = false
        calculateFare()
    }
}