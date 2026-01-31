package com.example.parte_mapa.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parte_mapa.MachineData
import com.example.parte_mapa.R


class MachineSegAdapter (private var machineList: List<MachineData>) :
    RecyclerView.Adapter<MachineSegAdapter.MachineSegViewHolder>() {



    // El ViewHolder se encarga de obtener las referencias de los controles del layout machine_info_window.xml
    inner class MachineSegViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val machineImage: ImageView = itemView.findViewById(R.id.machine_image)
        val machineTimeOnSite: TextView = itemView.findViewById(R.id.machine_timeonsite)
        val machineName: TextView = itemView.findViewById(R.id.machine_name)
        val machineParking: TextView = itemView.findViewById(R.id.machine_parking)
        val machinesKeyin: ImageView = itemView.findViewById(R.id.machines_keyin)
        val batteryTime: TextView = itemView.findViewById(R.id.machines_batterytime)
        val machineAddress: TextView = itemView.findViewById(R.id.machine_address)

    }

    // Crea un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineSegViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.machine_info_window, parent, false)
        return MachineSegViewHolder(view)
    }

    // Vincula los datos de cada máquina con el ViewHolder
    override fun onBindViewHolder(holder: MachineSegViewHolder, position: Int) {
        val machine = machineList[position]

        // Asigna la imagen según el tipo de máquina
        if(machine.type.equals("Contrapesada termica")){
            holder.machineImage.setImageResource(R.drawable.carretilla)

        }else if(machine.type.equals("Recogepedidos")){
            holder.machineImage.setImageResource(R.drawable.recogepedidos)

        }else if(machine.type.equals("Retractiles")){
            holder.machineImage.setImageResource(R.drawable.retractiles)

        }else if(machine.type.equals("Transpaletas")){
            holder.machineImage.setImageResource(R.drawable.transpaletas)

        }

        // Formatea el tiempo en formato legible
        holder.machineTimeOnSite.text = formatNumber(machine.timeOnSite)

        // Asigna el nombre de la máquina
        holder.machineName.text = machine.name

        //En caso de que tenga parking mostrara el icono, en caso contrario no
        if(machine.parking==false){
            holder.machineParking.visibility = View.GONE
        }else{
            holder.machineParking.visibility = View.VISIBLE
        }

        //En caso de que tenga keyin mostrara el icono, en caso contrario no
        if(machine.keysIn==false){
            holder.machinesKeyin.setImageResource(R.drawable.nokey)
        }else{
            holder.machinesKeyin.setImageResource(R.drawable.llave)
        }

        // Formatea el tiempo de batería en formato legible
        holder.batteryTime.text = formatNumber(machine.batteryTime)

        //Asigna la ubicacion de la maquina
        holder.machineAddress.text = machine.address
    }

    //Obtiene la cantidad de elementos en la lista
    override fun getItemCount(): Int = machineList.size

    // Función que convierte minutos en formato legible (días, horas, minutos)
    fun formatNumber(minutes: Int): String {
        val days = minutes / (24 * 60)
        val hours = (minutes % (24 * 60)) / 60
        val mins = minutes % 60

        val parts = mutableListOf<String>()
        if (days > 0) parts.add("$days d")
        if (hours > 0) parts.add("$hours h")
        if (mins > 0 || parts.isEmpty()) parts.add("$mins min")

        return parts.joinToString(" ")

    }

    // Actualiza la lista interna y redibuja el RecyclerView
    fun updateMachines(machineListNueva: List<MachineData>) {
        this.machineList = machineListNueva
        notifyDataSetChanged()
    }

}