package com.manish.car_parkingtrack_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.graphics.Color // Import this
import androidx.core.view.WindowCompat // Import this
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.manish.car_parkingtrack_app.Screen.CargoScreen
import com.manish.car_parkingtrack_app.Screen.CarparkScreen
import com.manish.car_parkingtrack_app.ui.theme.Car_Parkingtrack_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.statusBarColor = Color.BLACK
//
//        WindowCompat.getInsetsController(window, window.decorView).apply {
//            isAppearanceLightStatusBars = false
//        }

        enableEdgeToEdge()
        setContent {
            Navigator(CarparkScreen)
        }
    }
}
