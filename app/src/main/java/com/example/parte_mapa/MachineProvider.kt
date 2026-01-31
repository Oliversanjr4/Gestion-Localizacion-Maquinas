package com.example.parte_mapa


import org.osmdroid.util.GeoPoint


class MachineProvider {
    companion object {
        //Lista con el registro de todas las maquinas
        val machineList = listOf<MachineData>(
            MachineData(
                GeoPoint(48.856613, 2.352222), "Máquina 1 Paris", "Avenue des Champs-Élysées, Paris",
                timeOnSite = 120, keysIn = true, parking = true, batteryTime = 85,
                type = "Contrapesada térmica"
            ),
            MachineData(
                GeoPoint(51.507351, -0.127758), "Maquina 1 UK", "10 Downing St, London",
                timeOnSite = 95, keysIn = false, parking = true, batteryTime = 70,
                type = "Recogepedidos"
            ),
            MachineData(
                GeoPoint(40.416775, -3.703790), "Máquina 1 Madrid", "Calle de Alcalá, 1, 28014 Madrid, Madrid, España",
                timeOnSite = 180, keysIn = true, parking = false, batteryTime = 65,
                type = "Retráctiles"
            ),
            MachineData(
                GeoPoint(40.420177, -3.703928), "Máquina 2 Madrid", "Gran Vía, 28, 28013 Madrid, Madrid, España",
                timeOnSite = 140, keysIn = true, parking = true, batteryTime = 90,
                type = "Transpaletas"
            ),
            MachineData(
                GeoPoint(40.454545, -3.692083), "Máquina 3 Madrid", "Paseo de la Castellana, 156, 28046 Madrid, Madrid, España",
                timeOnSite = 100, keysIn = false, parking = false, batteryTime = 75,
                type = "Recogepedidos"
            ),
            MachineData(
                GeoPoint(40.435000, -3.687000), "Máquina 4 Madrid", "Calle de Serrano, 75, 28006 Madrid, Madrid, España",
                timeOnSite = 160, keysIn = true, parking = true, batteryTime = 80,
                type = "Retráctiles"
            ),
            MachineData(
                GeoPoint(40.426500, -3.675000), "Máquina 5 Madrid", "Calle de Goya, 63, 28001 Madrid, Madrid, España",
                timeOnSite = 110, keysIn = true, parking = false, batteryTime = 55,
                type = "Transpaletas"
            ),
            MachineData(
                GeoPoint(36.719136, -4.421384), "Máquina 6 Málaga", "Calle Marqués de Larios, 7, 29005 Málaga, Málaga, España",
                timeOnSite = 130, keysIn = false, parking = true, batteryTime = 88,
                type = "Contrapesada térmica"
            ),
            MachineData(
                GeoPoint(41.381800, 2.173900), "Máquina 7 Barcelona", "La Rambla, 91, 08002 Barcelona, Barcelona, España",
                timeOnSite = 150, keysIn = true, parking = true, batteryTime = 92,
                type = "Recogepedidos"
            ),
            MachineData(
                GeoPoint(37.389000, -5.994000), "Máquina 8 Sevilla", "Calle Sierpes, 50, 41004 Sevilla, Sevilla, España",
                timeOnSite = 105, keysIn = false, parking = false, batteryTime = 60,
                type = "Transpaletas"
            ),
            MachineData(
                GeoPoint(42.818500, -1.645800), "Máquina 9 Pamplona", "Calle Estafeta, 50, 31001 Pamplona, Navarra, España",
                timeOnSite = 170, keysIn = true, parking = true, batteryTime = 78,
                type = "Retráctiles"
            ),
            MachineData(
                GeoPoint(42.466315, -2.4394405), "Máquina 10 Logroño", "Calle San Juan, 19, 26001 Logroño, La Rioja, España",
                timeOnSite = 90, keysIn = false, parking = true, batteryTime = 66,
                type = "Contrapesada térmica"
            )
        )

    }
}