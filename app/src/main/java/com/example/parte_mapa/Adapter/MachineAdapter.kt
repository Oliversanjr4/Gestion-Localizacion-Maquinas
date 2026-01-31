package com.example.parte_mapa.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parte_mapa.MachineData
import com.example.parte_mapa.MachineViewHolder
import com.example.parte_mapa.R

class MachineAdapter(private var machineLista: List<MachineData>,
                     private val fragmentManager: FragmentManager,
                     private val onItemClick: (MachineData) -> Unit


) : RecyclerView.Adapter<MachineViewHolder>() {

    // Crea un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.machine_list_item, parent, false)
        return MachineViewHolder(view, fragmentManager, onItemClick)
    }

    // Vincula los datos de cada máquina con el ViewHolder
    override fun onBindViewHolder(holder: MachineViewHolder, position: Int) {
        val item = machineLista[position]
        holder.render(item)


    }

    //Obtiene la cantidad de elementos en la lista
    override fun getItemCount(): Int = machineLista.count()

    // Actualiza la lista interna y redibuja el RecyclerView
    fun updateMachines(machineListNueva: List<MachineData>) {
        this.machineLista = machineListNueva
        notifyDataSetChanged()
    }
}