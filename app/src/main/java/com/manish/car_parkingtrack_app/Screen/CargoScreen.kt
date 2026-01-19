package com.manish.car_parkingtrack_app.Screen

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
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
        val viewModel = remember { CargoModel(CargoService(context)) }

        CargoView(viewModel, navigator)
    }

    @Composable
    fun CargoView(viewModel: CargoModel, navigator: Navigator) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var showReceipt by remember { mutableStateOf(false) }

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
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    "Exit Desk",
                    color = BlueLight,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Smart retrieval via Phone or Plate",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // --- [OLD UI PATTERN - KEPT AS COMMENT FOR SENIOR TO SEE HISTORY] ---
                /*
                               /*
            OutlinedTextField(
                            value = viewModel.carnumber,
                            onValueChange = { viewModel.carnumber = it.uppercase() },
                            label = { Text("Vehicles Plate Number") },
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
            */

                */

                // --- [NEW UI PATTERN: Smart Unified Search] ---
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = viewModel.searchQuery,
                            onValueChange = { viewModel.searchQuery = it.uppercase() },
                            label = { Text("Phone or Plate Number") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = BlueLight) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            placeholder = { Text("Ex: 9876... or DL01...") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val matchCount = viewModel.checkcar()
                            if (matchCount == 1) {
                                showReceipt = true
                            } else if (matchCount == 0) {
                                Toast.makeText(context, viewModel.getcarerror ?: "Not found", Toast.LENGTH_SHORT).show()
                            }
                            // Note: if count > 1, showVehicleSelector is triggered in ViewModel
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueLight),
                    enabled = viewModel.searchQuery.isNotBlank() && !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("GENERATE BILL", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { navigator.pop() }) {
                    Text("Cancel & Go Back", color = Color.Gray)
                }
            }

            // --- 1. MULTIPLE VEHICLE SELECTOR (Added for Senior Suggestion) ---
            if (viewModel.showVehicleSelector) {
                AlertDialog(
                    onDismissRequest = { viewModel.showVehicleSelector = false },
                    title = { Text("Select Vehicle", fontWeight = FontWeight.Black) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            viewModel.foundCars.forEach { car ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        viewModel.selectCar(car)
                                        showReceipt = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                                ) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.DirectionsCar, null, tint = BlueLight)
                                        Column(modifier = Modifier.padding(start = 12.dp)) {
                                            Text(text = car.CarNumber, fontWeight = FontWeight.Bold)
                                            Text(text = "In: ${car.EntryTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}", fontSize = 11.sp)
                                        }
                                        Spacer(Modifier.weight(1f))
                                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.LightGray)
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = { TextButton(onClick = { viewModel.showVehicleSelector = false }) { Text("CLOSE") } },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(28.dp)
                )
            }

            // --- 2. THE ULTRA-PREMIUM TICKET DIALOG ---
            if (showReceipt && viewModel.foundCar != null) {
                Dialog(onDismissRequest = { showReceipt = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                    AnimatedVisibility(visible = showReceipt, enter = scaleIn() + fadeIn(), exit = scaleOut() + fadeOut()) {
                        Box(modifier = Modifier.fillMaxWidth(0.85f).wrapContentHeight(), contentAlignment = Alignment.Center) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
                            ) {
                                Column(modifier = Modifier.padding(bottom = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(modifier = Modifier.fillMaxWidth().background(BlueLight).padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(Icons.Default.DirectionsCar, null, tint = Color.White, modifier = Modifier.size(32.dp))
                                            Text("OFFICIAL RECEIPT", color = Color.White, fontWeight = FontWeight.Black, letterSpacing = 2.sp, fontSize = 12.sp)
                                        }
                                    }

                                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        TicketRow("OWNER", viewModel.foundCar?.OwerName?.uppercase() ?: "GUEST")
                                        TicketRow("VEHICLE ID", viewModel.foundCar?.CarNumber ?: "--")
                                        TicketRow("PHONE", viewModel.foundCar?.PhoneNO ?: "--")
                                        TicketRow("ENTRY TIME", viewModel.foundCar?.EntryTime?.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a")) ?: "--")

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Perforated Line
                                        Box(modifier = Modifier.fillMaxWidth().height(40.dp), contentAlignment = Alignment.Center) {
                                            Canvas(Modifier.fillMaxWidth().height(1.dp)) { drawLine(Color.LightGray, androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(size.width, 0f), pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)) }
                                            Box(modifier = Modifier.size(24.dp).offset(x = (-36).dp).background(Color(0xFFE3F2FD), CircleShape).align(Alignment.CenterStart))
                                            Box(modifier = Modifier.size(24.dp).offset(x = (36).dp).background(Color.White, CircleShape).align(Alignment.CenterEnd))
                                        }

                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                            Column { Text("DURATION", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold); Text(viewModel.getParkingDuration(), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
                                            Column(horizontalAlignment = Alignment.End) { Text("TOTAL FARE", color = BlueLight, fontSize = 10.sp, fontWeight = FontWeight.Bold); Text("₹${viewModel.totalFare}", fontWeight = FontWeight.Black, fontSize = 32.sp) }
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))
                                        Button(
                                            onClick = {
                                                showReceipt = false
                                                navigator.push(PaymentScreen(viewModel.totalFare, viewModel.foundCar!!.CarNumber))
                                            },
                                            modifier = Modifier.fillMaxWidth().height(56.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                        ) { Text("PROCEED TO PAY", color = Color.White) }

                                        Text(text = "CLOSE", modifier = Modifier.padding(top = 12.dp).align(Alignment.CenterHorizontally).clickable { showReceipt = false }, color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- HELPER COMPOSABLE (NOW UNCOMMENTED AND WORKING) ---
    @Composable
    private fun TicketRow(label: String, value: String) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.DarkGray)
        }
    }
}