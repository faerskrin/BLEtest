package com.example.bletest

data class BleEntity(
    val id: String,
    val rssi: Int,
    val uuid: String? = null,
    val major: String = "",
    val manor: String = "",
    val txPower: Int = 0,
    val distance: Double = 0.0
)