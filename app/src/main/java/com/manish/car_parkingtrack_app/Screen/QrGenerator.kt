package com.manish.car_parkingtrack_app.Screen // Make sure package matches your folder

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

// This generates the actual pixels
fun generateQrCode(text: String): Bitmap {
    val size = 512
    val writer = QRCodeWriter()
    // This creates a math matrix of the QR
    val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

    for (x in 0 until size) {
        for (y in 0 until size) {
            // Fill black for bits, white for empty
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
        }
    }
    return bitmap
}