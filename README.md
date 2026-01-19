# 🚗 Smart Car Parking & Analyst Manager

A professional-grade Android application built with **Jetpack Compose** and **Room Database**. Recently refactored to transition from a basic utility into a comprehensive **Business Intelligence (BI)** tool for parking management.

## 🚀 What's New (Version 2.0)

Based on senior developer feedback, this version introduces advanced logic and executive-level features:

### 🔍 1. Smart Unified Search (Flexible ID)
*   **Old Way:** Required both Phone and Plate number to find a record.
*   **New Way:** A single "Smart Search" terminal. Type **either** the phone number or the car plate. The database uses `OR` logic to find your vehicle instantly.

### 📊 2. Analyst Dashboard
*   A new dedicated screen for managers to see the "Big Picture."
*   **Total Volume:** Real-time count of all parked vehicles.
*   **Revenue Projection:** Automated calculation of total earnings based on turnover.
*   **System Health:** Live tracking of database status and peak hours.

### 🍱 3. Multi-Vehicle Support (Collision Handling)
*   The app now detects if a single user (one phone number) has multiple cars parked.
*   Introduces a **Vehicle Selection UI** allowing the guard to pick the correct car from a list before generating a bill.

---

## ✨ Core Features

- **Ceiling Fare Logic:** Accurate billing based on a **₹10 per 10-minute** rule (automatically rounds up).
- **Ultra-Premium Receipt:** Animated digital ticket with a unique "notched paper" design and decorative barcode.
- **UPI QR Terminal:** Modern checkout screen with instant QR generation for GPay, PhonePe, and Paytm.
- **Glassmorphism UI:** Premium Material 3 design with smooth color transitions and elevation.

## 📥 Download Latest Version
[**Download App (app-debug.apk)**](https://github.com/ManishT-coder/Car_Parkingtrack_App/raw/master/app-debug.apk)

---

## 🛠️ Tech Stack & Evolution

| Layer | Implementation | Remarks |
| :--- | :--- | :--- |
| **UI** | Jetpack Compose | Modern, reactive, and fully animated. |
| **Logic** | MVVM Pattern | Clean separation between UI and Database. |
| **Database** | Room (SQLite) | Optimized with KSP for high performance. |
| **Queries** | OR-Logic Search | Refactored for better User Experience (UX). |

## 🎮 How to use the Analyst View
1. Open the App.
2. Click the **Analytics** icon on the main screen.
3. View your total vehicle counts and estimated revenue instantly.

---

## 👨‍💻 Developed By
**Manish Tigaya**  
*Building professional software solutions with modern tech.*

[GitHub Profile](https://github.com/ManishT-coder)
