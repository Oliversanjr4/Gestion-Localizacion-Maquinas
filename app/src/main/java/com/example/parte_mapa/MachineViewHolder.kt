package com.example.parte_mapa

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parte_mapa.databinding.MachineListItemBinding



class MachineViewHolder(view: View, val fragmentManager: FragmentManager,  private val onItemClick: (MachineData) -> Unit): RecyclerView.ViewHolder(view) {
    val binding  = MachineListItemBinding.bind(view)

    fun render(
        machineModel: MachineData,

        ){

        binding.machineName.text = machineModel.name
        binding.machineAddress.text = machineModel.address

        
        itemView.setOnClickListener {

            onItemClick(machineModel)



            val bottomSheet = MachineBottomSheet.newInstance(
                machineModel.name ,
                machineModel.address,
                machineModel.type,
                machineModel.timeOnSite,
                machineModel.keysIn,
                machineModel.parking,
                machineModel.batteryTime)

            bottomSheet.show(fragmentManager, "MachineInfoBottomSheet")
            itemView.rootView.findViewById<RecyclerView>(R.id.RecyclerView).visibility = View.GONE
        }


    }
}