package com.manish.car_parkingtrack_app.Screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.manish.car_parkingtrack_app.Database.service.CarparkService
import com.manish.car_parkingtrack_app.ui.theme.BlueLight
import kotlinx.coroutines.launch

class DashboardScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.current
        var totalCars by remember { mutableStateOf(0) }
        val service = remember { CarparkService(context) }

        // Fetch Data automatically
        LaunchedEffect(Unit) {
            totalCars = service.getTotalCount()
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text("MANAGER DASHBOARD", fontWeight = FontWeight.Black) })
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF8FAFC)).padding(24.dp)) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Card 1: Total Volume
                    AnalyticsCard("Total Vehicles", totalCars.toString(), Icons.Default.DirectionsCar, BlueLight, Modifier.weight(1f))
                    // Card 2: Revenue Estimation
                    AnalyticsCard("Estimated Revenue", "₹${totalCars * 20}", Icons.Default.Payments, Color(0xFF4CAF50), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Detailed Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Operational Health", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("All systems active and syncing", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(progress = 1f, modifier = Modifier.fillMaxWidth(), color = Color.Green)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { navigator?.pop() }, modifier = Modifier.fillMaxWidth()) { Text("BACK TO TERMINAL") }
            }
        }
    }

    @Composable
    fun AnalyticsCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
        Card(modifier = modifier, shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Icon(icon, null, tint = color)
                Text(label, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                Text(value, fontSize = 22.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}