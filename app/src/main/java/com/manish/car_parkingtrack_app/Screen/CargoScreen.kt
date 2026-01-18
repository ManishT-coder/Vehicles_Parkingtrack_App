package com.manish.car_parkingtrack_app.Screen

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.manish.car_parkingtrack_app.Database.service.CargoService
import com.manish.car_parkingtrack_app.Model.CargoModel
import com.manish.car_parkingtrack_app.ui.theme.BlueLight
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

object CargoScreen : Screen {
    private fun readResolve(): Any = CargoScreen

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = remember(context) { CargoModel(CargoService(context)) }

        CargoView(viewModel, navigator)
    }

    @Composable
    private fun CargoView(viewModel: CargoModel, navigator: Navigator) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var showDialog by remember { mutableStateOf(false) }

        val phoneRegex = "^[0-9]{10}$".toRegex()
        val isPhoneError = !viewModel.phoneno.matches(phoneRegex) && viewModel.phoneno.isNotEmpty()
        val isFormValid = viewModel.carnumber.isNotBlank() &&
                viewModel.phoneno.length == 10 &&
                !viewModel.isLoading

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

                // --- HEADER ---
                Text(
                    text = "Vehicle Exit",
                    color = BlueLight,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Process retrieval & calculate fare",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // --- FORM CARD ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = viewModel.carnumber,
                            onValueChange = { viewModel.carnumber = it.uppercase() },
                            label = { Text("Car Plate Number") },
                            leadingIcon = { Icon(Icons.Default.DirectionsCar, null, tint = BlueLight) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )

                        OutlinedTextField(
                            value = viewModel.phoneno,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 10) {
                                    viewModel.phoneno = input
                                }
                            },
                            label = { Text("Registered Phone") },
                            leadingIcon = { Icon(Icons.Default.Phone, null, tint = BlueLight) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = isPhoneError,
                            supportingText = { if (isPhoneError) Text("Must be 10 digits") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // --- SEARCH BUTTON ---
                Button(
                    onClick = {
                        scope.launch {
                            if (viewModel.checkcar()) {
                                showDialog = true
                            } else {
                                Toast.makeText(context, viewModel.getcarerror ?: "Record not found", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
                    enabled = isFormValid
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("GENERATE RECEIPT", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { navigator.pop() }) {
                    Text("Cancel & Go Back", color = Color.Gray)
                }
            }

            // --- THE DYNAMIC TICKET DIALOG ---
            // --- THE "ULTRA-PREMIUM" TICKET DIALOG ---
            if (showDialog) {
                Dialog(
                    onDismissRequest = { showDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    AnimatedVisibility(
                        visible = showDialog,
                        enter = scaleIn() + fadeIn() + expandVertically(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .wrapContentHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Main Ticket Body
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(bottom = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // 1. TOP SECTION (Blue Header)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(BlueLight)
                                            .padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.DirectionsCar, null, tint = Color.White, modifier = Modifier.size(32.dp))
                                            Text("OFFICIAL RECEIPT", color = Color.White, fontWeight = FontWeight.Black, letterSpacing = 2.sp, fontSize = 12.sp)
                                        }
                                    }

                                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        // 2. DATA ROWS
                                        TicketRow("OWNER", viewModel.foundCar?.OwerName?.uppercase() ?: "GUEST")
                                        TicketRow("VEHICLE ID", viewModel.carnumber)
                                        TicketRow("PHONE", viewModel.phoneno)
                                        TicketRow("ENTRY TIME", viewModel.foundCar?.EntryTime?.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a")) ?: "--")

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // 3. THE PERFORATED LINE WITH NOTCHES
                                        Box(
                                            modifier = Modifier.fillMaxWidth().height(40.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                                                drawLine(
                                                    color = Color.LightGray,
                                                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f),
                                                    strokeWidth = 2.dp.toPx()
                                                )
                                            }
                                            // Left Notch
                                            Box(modifier = Modifier.size(24.dp).offset(x = (-36).dp).background(Color(0xFFE3F2FD), CircleShape).align(Alignment.CenterStart))
                                            // Right Notch
                                            Box(modifier = Modifier.size(24.dp).offset(x = (36).dp).background(Color.White, CircleShape).align(Alignment.CenterEnd))
                                        }

                                        // 4. FARE SUMMARY
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text("DURATION", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                Text(viewModel.getParkingDuration(), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.DarkGray)
                                            }
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text("TOTAL FARE", color = BlueLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                Text("₹${viewModel.totalFare}", fontWeight = FontWeight.Black, fontSize = 32.sp, color = Color.Black)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // 5. ACTION BUTTON
                                        Button(
                                            onClick = {
                                                showDialog = false
                                                // Navigate to Payment Screen with Amount and Car Number
                                                navigator.push(PaymentScreen(viewModel.totalFare, viewModel.carnumber))                                            },
                                            modifier = Modifier.fillMaxWidth().height(56.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                        ) {
                                            Text("PROCEED TO PAY", fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "CLOSE",
                                            modifier = Modifier.padding(top = 12.dp).align(Alignment.CenterHorizontally).clickable { showDialog = false },
                                            color = Color.LightGray,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- HELPER COMPOSABLE (Moved outside the main Box scope) ---
    // Updated Helper Function for the Ticket
    @Composable
    private fun TicketRow(label: String, value: String) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.DarkGray)
        }
    }
}