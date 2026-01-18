package com.manish.car_parkingtrack_app.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.manish.car_parkingtrack_app.Database.service.CarparkService
import com.manish.car_parkingtrack_app.Model.CarparkModel
import com.manish.car_parkingtrack_app.ui.theme.BlueLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

object CarparkScreen : Screen {
    private fun readResolve(): Any = CarparkScreen

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = remember(context) { CarparkModel(CarparkService(context)) }

        CarparkView(viewModel, navigator)
    }

    @Composable
    private fun CarparkView(viewModel: CarparkModel, navigator: Navigator) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var isLoading by remember { mutableStateOf(false) } // Added Loading state

        val phoneRegex = "^[0-9]{10}$".toRegex()
        val isPhoneError = !viewModel.phoneno.matches(phoneRegex) && viewModel.phoneno.isNotEmpty()

        // Form is valid if name, car number are filled and phone is exactly 10 digits
        val isFormValid = viewModel.OwerName.isNotBlank() &&
                viewModel.carnumber.isNotBlank() &&
                viewModel.phoneno.length == 10 && !isLoading

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color.White)
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "Park Your Vehicle",
                    color = BlueLight,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Assign a spot and record entry",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = viewModel.OwerName,
                            onValueChange = { viewModel.OwerName = it },
                            label = { Text("Owner Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = BlueLight) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )

                        OutlinedTextField(
                            value = viewModel.carnumber,
                            onValueChange = { viewModel.carnumber = it.uppercase() }, // Auto Uppercase for car plates
                            label = { Text("Car Plate Number") },
                            leadingIcon = { Icon(Icons.Default.DirectionsCar, null, tint = BlueLight) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )

                        OutlinedTextField(
                            value = viewModel.phoneno,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 10) {
                                    viewModel.phoneno = input
                                }
                            },
                            label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, null, tint = BlueLight) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = isPhoneError,
                            supportingText = { if (isPhoneError) Text("Must be 10 digits") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 2. ACTION BUTTON WITH LOADING
                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            try {
                                // Use the function we created in your ViewModel
                                val success = viewModel.parking()

                                if (success) {
                                    Toast.makeText(context, "Car Parked Successfully!", Toast.LENGTH_SHORT).show()
                                    delay(500) // Small delay for better UX
                                    navigator.pop()
                                viewModel.clearFields()// Go back to the previous screen
                                } else {
                                    // Show error from ViewModel if database save failed
                                    val errorMsg = viewModel.parkerror ?: "Unknown Error"
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "System Error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false // Turn off loading spinner
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
                    enabled = isFormValid // Button is only clickable if form is correct
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("CONFIRM PARKING", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))


                Text("I already park my car ", Modifier.clickable{navigator.push(CargoScreen)})
            }
        }
    }
}