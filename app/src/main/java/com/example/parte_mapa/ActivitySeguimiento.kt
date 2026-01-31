package com.example.parte_mapa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parte_mapa.Adapter.MachineSegAdapter
import com.example.parte_mapa.databinding.ActivitySeguimientoBinding

class ActivitySeguimiento : AppCompatActivity() {
    lateinit var binding: ActivitySeguimientoBinding
    private var machineMList: MutableList<MachineData> = MachineProvider.machineList.toMutableList() // Copia mutable para permitir filtrado sin afectar la fuente original
    private lateinit var recyclerView: RecyclerView
    private lateinit var machineAdapter: MachineSegAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeguimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el RecyclerView y establece su LayoutManager (vertical en forma de lista)
        recyclerView =binding.RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)




        // Inicializa el adapter y lo asigna al RecyclerView
        machineAdapter = MachineSegAdapter(machineMList)
        recyclerView.adapter = machineAdapter

        //Gestiona el bottomNavigationView para cambiar entre pantallas
        val bottomNav = binding.bottomNavigationView
        bottomNav.selectedItemId = R.id.Seguimiento
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.MapaI -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }


                else -> false
            }
        }

        //Cuadro de busqueda para filtrar en tiempo real la lista por nombre, dirección o tipo
        binding.busqueda.addTextChangedListener { busqueda ->
            //Filtramos en la lista de las maquinas, si la busqueda esta vacia, mostramos todas las maquinas
            val maquinas_filtradas = machineMList.filter { maquina ->
                maquina.name.lowercase().contains(busqueda.toString().lowercase()) ||
                        maquina.address.lowercase().contains(busqueda.toString().lowercase())||
                        maquina.type.lowercase().contains(busqueda.toString().lowercase())



            }
            // Actualiza el adapter con la lista filtrada
            machineAdapter.updateMachines(maquinas_filtradas)

            recyclerView.requestLayout() // Fuerza un redibujado del RecyclerView para reflejar cambios
        }




    }


}