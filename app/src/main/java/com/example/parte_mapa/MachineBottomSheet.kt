package com.example.parte_mapa


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.osmdroid.util.GeoPoint

class MachineBottomSheet : BottomSheetDialogFragment() {

    private var machineName: String? = null
    private var machineAddress: String? = null
    private var machineType: String? = null
    private var machineTimeOnSite: Int? = null
    private var machineKeysIn: Boolean? = null
    private var machineParking: Boolean? = null
    private var machineBatteryTime: Int? = null


    companion object {
        fun newInstance(
            name: String,
            address: String,
            type: String,
            timeonSite: Int,
            keysIn: Boolean,
            parking: Boolean,
            batteryTime: Int
        ): MachineBottomSheet {
            val fragment = MachineBottomSheet()
            val args = Bundle()
            args.putString("name", name)
            args.putString("address", address)
            args.putString("type", type)
            args.putInt("timeonSite", timeonSite)
            args.putBoolean("keysIn", keysIn)
            args.putBoolean("parking", parking)
            args.putInt("batteryTime", batteryTime)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            machineName = it.getString("name")
            machineAddress = it.getString("address")
            machineType = it.getString("type")
            machineTimeOnSite = it.getInt("timeonSite")
            machineKeysIn = it.getBoolean("keysIn")
            machineParking = it.getBoolean("parking")
            machineBatteryTime = it.getInt("batteryTime")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.machine_info_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.machine_name).text = machineName
        view.findViewById<TextView>(R.id.machine_address).text = machineAddress

        if(machineParking==false){
            view.findViewById<TextView>(R.id.machine_parking).visibility = View.GONE
        }else{
            view.findViewById<TextView>(R.id.machine_parking).visibility = View.VISIBLE
        }

        if(machineKeysIn==false){
            view.findViewById<ImageView>(R.id.machines_keyin).setImageResource(R.drawable.nokey)
        }else{
            view.findViewById<ImageView>(R.id.machines_keyin).setImageResource(R.drawable.llave)
        }




        view.findViewById<TextView>(R.id.machines_batterytime).text = formatBatteryTime(
            machineBatteryTime?.toInt() ?: 0
        )

        view.findViewById<TextView>(R.id.machine_timeonsite).text = formatBatteryTime(
            machineTimeOnSite?.toInt() ?: 0
        )

        if(machineType.equals("Contrapesada termica")){
            view.findViewById<ImageView>(R.id.machine_image).setImageResource(R.drawable.carretilla)
        }else if(machineType.equals("Recogepedidos")){
            view.findViewById<ImageView>(R.id.machine_image).setImageResource(R.drawable.recogepedidos)
        }else if(machineType.equals("Retractiles")){
            view.findViewById<ImageView>(R.id.machine_image).setImageResource(R.drawable.retractiles)
        }else if(machineType.equals("Transpaletas")){
            view.findViewById<ImageView>(R.id.machine_image).setImageResource(R.drawable.transpaletas)
        }


    }

    //Dado que viene en minutos los tiempos, pasarlo a los distintos tiempos
    fun formatBatteryTime(minutes: Int): String {
        val days = minutes / (24 * 60)
        val hours = (minutes % (24 * 60)) / 60
        val mins = minutes % 60


        val parts = mutableListOf<String>()
        if (days > 0) parts.add("$days d")
        if (hours > 0) parts.add("$hours h")
        if (mins > 0 || parts.isEmpty()) parts.add("$mins min")

        return parts.joinToString(" ")
    }
}
