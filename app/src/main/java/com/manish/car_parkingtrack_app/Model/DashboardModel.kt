package com.manish.car_parkingtrack_app.Model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.manish.car_parkingtrack_app.Database.service.CarparkService
class DashboardModel(private val service: CarparkService) : ViewModel() {
    var totalVehicles by mutableStateOf(0)
    var estimatedRevenue by mutableStateOf(0)

    suspend fun fetchStats() {
        val allEntries = service.getAllEntries() // Assuming this returns List<Carpark>
        totalVehicles = allEntries.size

        // Example logic: Assume an average of 10 Rupees per car for the dashboard estimate
        estimatedRevenue = totalVehicles * 10
    }
}

