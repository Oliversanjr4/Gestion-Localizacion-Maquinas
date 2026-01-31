package com.example.parte_mapa

import org.osmdroid.util.GeoPoint

data class MachineData(

    val location: GeoPoint,
    val name: String,
    val address: String,
    val type: String,
    val timeOnSite: Int,
    val keysIn: Boolean,
    val parking: Boolean,
    val batteryTime: Int

)
