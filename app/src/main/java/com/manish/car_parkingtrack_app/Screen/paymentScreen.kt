package com.manish.car_parkingtrack_app.Screen

import android.widget.Toast
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.manish.car_parkingtrack_app.ui.theme.BlueLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder

data class PaymentScreen(val amount: Int, val carNumber: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        // Simple State: Which method is clicked?
        var selectedMethod by remember { mutableStateOf("UPI") }
        var isProcessing by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Payment Methods", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- 1. AMOUNT DISPLAY ---
                Spacer(modifier = Modifier.height(20.dp))
                Text("Total to Pay", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "₹$amount",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
                Text(
                    text = "Vehicle: $carNumber",
                    color = BlueLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- 2. PAYMENT METHODS ---
                Text(
                    "Choose Payment Method",
                    modifier = Modifier.align(Alignment.Start),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // UPI Option Card
                SimpleMethodCard(
                    title = "UPI / Scan QR",
                    icon = Icons.Default.QrCode,
                    isSelected = selectedMethod == "UPI",
                    onSelect = { selectedMethod = "UPI" }
                )

                // Cash Option Card
                SimpleMethodCard(
                    title = "Cash at Counter",
                    icon = Icons.Default.Payments,
                    isSelected = selectedMethod == "CASH",
                    onSelect = { selectedMethod = "CASH" }
                )

                // --- 3. QR CODE LOGIC (Only shows if UPI is selected) ---
                if (selectedMethod == "UPI") {
                    Spacer(modifier = Modifier.height(30.dp))

                    val encodedName = URLEncoder.encode("Manish Tigaya", "UTF-8")
                    val upiData = "upi://pay?pa=mayaatigaya@oksbi&pn=$encodedName&am=$amount&cu=INR"

                    QrCodeDisplay(data = upiData)

                    Text(
                        "Scan using any UPI App",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // --- 4. CONFIRM BUTTON ---
                Button(
                    onClick = {
                        isProcessing = true
                        scope.launch {
                            delay(1500) // Pretend to process
                            Toast.makeText(context, "Payment Success! Exit Granted.", Toast.LENGTH_SHORT).show()
                            navigator?.popUntilRoot() // Go back to Home
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        val btnText = if (selectedMethod == "UPI") "VERIFY & EXIT" else "CONFIRM CASH PAID"
                        Text(btnText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    @Composable
    private fun SimpleMethodCard(
        title: String,
        icon: ImageVector,
        isSelected: Boolean,
        onSelect: () -> Unit
    ) {
        // Change colors based on selection
        val cardColor = if (isSelected) BlueLight.copy(alpha = 0.1f) else Color.White
        val borderColor = if (isSelected) BlueLight else Color(0xFFEEEEEE)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { onSelect() }
                .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) BlueLight else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isSelected) BlueLight else Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(selected = isSelected, onClick = onSelect)
            }
        }
    }

    @Composable
    fun QrCodeDisplay(data: String) {
        val qrBitmap = remember(data) {
            try {
                generateQrCode(data).asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }

        if (qrBitmap != null) {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.size(200.dp)
            ) {
                Image(
                    bitmap = qrBitmap,
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
        }
    }
}