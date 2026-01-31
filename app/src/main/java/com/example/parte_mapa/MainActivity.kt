package com.example.parte_mapa

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.GpsStatus
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parte_mapa.Adapter.MachineAdapter
import com.example.parte_mapa.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


//MapListener para detectar eventos en el mapa
//GpsStatus.Listener para recibir actualizaciones del estado del Gps
class MainActivity : AppCompatActivity(), MapListener, GpsStatus.Listener {

    //Representa el mapa
    private lateinit var mMap: MapView

    //Controlador para gestionar el zoom y los desplazamientos
    private lateinit var controller: IMapController

    //Capa que muestra la ubicacion del usuario
    private lateinit var mMyLocationOverlay: MyLocationNewOverlay

    //    private lateinit var originalLocations: MutableList<MachineData>

    //Lista de resultados de la busqueda
    private lateinit var recyclerView: RecyclerView

    //Toolbar
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    //Lista de todas las maquinas existentes
    private var machineMList: MutableList<MachineData> = MachineProvider.machineList.toMutableList()

    //Adaptador del recyclerView
    private lateinit var adapter: MachineAdapter

    //Binding
    private lateinit var binding: ActivityMainBinding

    //Manejador del recyclerView
    private val llmanager = LinearLayoutManager(this)

    //Barra de busqueda
    private lateinit var  busqueda: TextInputEditText

    //Autorizacion de permisos
    private val LOCATION_PERMISSION_REQUEST = 1

    //Localizacion del usuario
   // private var userLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocation: GeoPoint? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Infla el diseño utilizando el ViewBinding(ActivityMainBinding)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializacion
        recyclerView = binding.RecyclerView
        busqueda = binding.busqueda


        //Carga la configuracion de osmdroid
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )


        mMap = binding.osmmap
        mMap.setTileSource(TileSourceFactory.MAPNIK) //Usa OpenStreetMap como proveedor de mapas
        mMap.mapCenter //Obtiene el centro del mapa
        mMap.setMultiTouchControls(true) //Habilita gestos multitactiles (zoom y desplazamiento)
        mMap.getLocalVisibleRect(Rect()) //Obtiene el rectangulo visible en el mapa

        //Crea y habilita una superposicion para mostrar la ubicacion del usuario
        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        controller = mMap.controller //Obtiene el controlador del mapa

        mMyLocationOverlay.enableMyLocation() //Habilita la visualizacion de la ubicacion del usuario
        mMyLocationOverlay.enableFollowLocation() //Permite seguir la ubicacion del usuario
        mMyLocationOverlay.isDrawAccuracyEnabled = true

        //Cuando el GPS obtiene la primera posicion, centra el mapa en la ubicacion del usuario
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(mMyLocationOverlay.myLocation);
                controller.animateTo(mMyLocationOverlay.myLocation)
            }
        }


        //Establece el zoom inicial
        controller.setZoom(6.0)


        //Agrega la capa de ubicacion y el listener de eventos del mapa
        mMap.overlays.add(mMyLocationOverlay)
        mMap.addMapListener(this)

        // Llamada a addMachinesToMap(), agregando marcadores en el mapa con ubicaciones predefinidas de las maquinas
        addMachinesToMap(machineMList)



        //Inicializacion del fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasLocationPermission()) {
            getUserLocation()
        } else {
            requestLocationPermission()
        }


        //Gestiona el bottomNavigationView
        val bottomNav = binding.bottomNavigationView
        bottomNav.selectedItemId = R.id.MapaI
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.Seguimiento -> {
                    startActivity(Intent(this, ActivitySeguimiento::class.java))
                    true
                }

                else -> false
            }
        }

        //La toolbar comienza con el cuadro de busqueda
        binding.TIL.visibility = View.VISIBLE
        binding.toolbarCustomContainer.visibility = View.GONE



        //Cuadro de busqueda para filtrar
        busqueda.addTextChangedListener { busqueda ->
            //Filtramos en la lista de las maquinas
            val maquinas_filtradas = machineMList.filter { maquina ->
                maquina.name.lowercase().contains(busqueda.toString().lowercase()) ||
                        maquina.address.lowercase().contains(busqueda.toString().lowercase())
            }
            // Actualiza el adapter con la lista filtrada
            adapter.updateMachines(maquinas_filtradas)

            //Si no hay resultados, oculta el recyclerView
            if(maquinas_filtradas.isNotEmpty()){
                binding.RecyclerView.visibility =  View.VISIBLE
            }else{
                binding.RecyclerView.visibility = View.GONE
            }


            //Ajustar el recyclerView para que tenfa una longitud variable en funcion del numero de items
            recyclerView.layoutParams = recyclerView.layoutParams.apply {
                //Queremos una altura de 60dp por item, pero hay que pasarlo a pixeles
                val itemHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    60f, // 60dp
                    recyclerView.context.resources.displayMetrics
                ).toInt()
                var itemCount = maquinas_filtradas.size

                if (itemCount > 3) {
                    height = itemHeight * 3

                }else {
                    height = itemHeight * itemCount

                }

            }
            recyclerView.requestLayout() // Fuerza un redibujado del RecyclerView para reflejar cambios
        }
        //Inicia el recyclerView
        initRecyclerView()

        //Si se pierde el foco en el cuadro de busqueda desaparece el recyclerview
        busqueda.setOnFocusChangeListener{_, hasFocus ->
            if(!hasFocus){
                binding.RecyclerView.visibility = View.GONE
            }

        }

        //Boton para cambiar la vista a mapa o satelite
        var isSatellite = false
        val btnVista = binding.btnVista
        btnVista.setOnClickListener {
            isSatellite = !isSatellite
            mMap.setTileSource(if (isSatellite) TileSourceFactory.USGS_SAT else TileSourceFactory.MAPNIK)
        }

        //Boton para volver a la ubicacion del usuario
        val userLocation = GeoPoint(40.38656, -3.5520512)
        val btnUbicacionOriginal = binding.btnOrigen

        btnUbicacionOriginal.setOnClickListener {
            vistaOriginal(userLocation)
        }

    }



    //Inicia el recyclerView
    private fun initRecyclerView() {
        adapter = MachineAdapter(machineMList, supportFragmentManager) { selectedMachine ->

            //Al seleccionar una maquina, el cuadro de busqueda se va
            binding.TIL.visibility = View.GONE

            //Y aparece el titulo con el nombre de la maquina
            binding.toolbarCustomContainer.visibility = View.VISIBLE
            binding.toolbarTextView.text = selectedMachine.name

            //Boton para volver
            binding.toolbarBtnBack.setOnClickListener {
                binding.TIL.visibility = View.VISIBLE
                binding.toolbarCustomContainer.visibility = View.GONE
            }

            //Zoom a la maquina
            focusOnMachine(selectedMachine)

        }
        recyclerView.layoutManager = llmanager
        recyclerView.adapter = adapter


    }




    //Sobreescribimos el metodo dispatchTouchEvent para que si se hace click fuera del editText o el recyclerView se pierda el foco
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus


            if (view is TextInputEditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)

                val recyclerRect = Rect()
                recyclerView.getGlobalVisibleRect(recyclerRect)

                // Si el toque está fuera del EditText y fuera del RecyclerView
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt()) &&
                    !recyclerRect.contains(event.rawX.toInt(), event.rawY.toInt())) {

                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } else {
                // Si se toca dentro del EditText, aseguramos que se muestre el teclado
                val touchedView = currentFocus ?: findViewById<View>(android.R.id.content)
                if (touchedView is TextInputEditText) {
                    touchedView.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(touchedView, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


    //Se ejecuta cuando el usuario mueve el mapa, imprimiendo la nueva posicion en los logs
    override fun onScroll(event: ScrollEvent?): Boolean {
        // event?.source?.getMapCenter()
        Log.d("TAG", "onCreate:la ${event?.source?.getMapCenter()?.latitude}")
        Log.d("TAG", "onCreate:lo ${event?.source?.getMapCenter()?.longitude}")
        //  Log.e("TAG", "onScroll   x: ${event?.x}  y: ${event?.y}", )
        return true
    }

    //Se ejecuta cuando el usuario hace zoom, imprimiendo el nivel de zoom en los logs.
    override fun onZoom(event: ZoomEvent?): Boolean {
        //  event?.zoomLevel?.let { controller.setZoom(it) }
        Log.d("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        //actualizarClusters(machineMList)
        return false;
    }

    //Obtiene el estado del GPS
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onGpsStatusChanged(event: Int) {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val gpsStatus = locationManager.getGpsStatus(null)

        //Maneja diferentes eventos del GPS e imprimimos los logs
        when (event) {
            GpsStatus.GPS_EVENT_STARTED -> {
                Log.i("GPS_STATUS", "GPS started")
            }

            GpsStatus.GPS_EVENT_STOPPED -> {
                Log.i("GPS_STATUS", "GPS stopped")
            }

            GpsStatus.GPS_EVENT_FIRST_FIX -> {
                val timeToFirstFix = gpsStatus?.timeToFirstFix
                Log.i("GPS_STATUS", "GPS first fix in $timeToFirstFix ms")
            }

            GpsStatus.GPS_EVENT_SATELLITE_STATUS -> {
                gpsStatus?.satellites?.forEach { satellite ->
                    Log.i("GPS_STATUS", "Satellite: ${satellite.prn}, SNR: ${satellite.snr}")
                }
            }
        }
    }

    //Amplia la vista mapa hasta la localizacion de la maquina
    fun focusOnMachine(machine: MachineData) {
        val mapController = mMap.controller
        val zoomLevel = 18.0 // Zoom deseado
        val animationSpeed = 1000L // Duración de la animación en ms

        mapController.animateTo(machine.location, zoomLevel, animationSpeed)
    }

    //Vuelve a la vista mapa desampliada donde esta el usuario
    fun vistaOriginal(userLocation: GeoPoint){
        val mapController = mMap.controller
        val zoomLevel = 6.0 // Zoom deseado
        val animationSpeed = 1000L // Duración de la animación en ms

        mapController.animateTo(userLocation, zoomLevel, animationSpeed)
    }



    //Recorre la lista de los puntos donde estan las maquinas
    fun addMachinesToMap(locations: MutableList<MachineData>) {
        //por cada una de las localizaciones en la lista, coge sus atributos
        locations.forEach { machine ->
            //Marcador
            val marker = Marker(mMap)
            //le da una posicion al marcador
            marker.position = machine.location
            //titulo para el marcador
            marker.title = machine.name
            //una descripcion que sera la direccion
            marker.subDescription = machine.address
            //y el icono del marcador
            marker.icon = resources.getDrawable(R.drawable.carricoche, null)
            //proporciones del marcador
            marker.setAnchor(
                org.osmdroid.views.overlay.Marker.ANCHOR_CENTER,
                org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM
            )

            //Cuando pinchas en el marcador, aparece una ventana abajo con mas informacion de la maquina
            marker.setOnMarkerClickListener { item, _ ->
                focusOnMachine(machine) // Llamamos a la misma función
                true
                val bottomSheet =
                    MachineBottomSheet.newInstance(
                        machine.name ,
                        machine.address,
                        machine.type,
                        machine.timeOnSite,
                        machine.keysIn,
                        machine.parking,
                        machine.batteryTime)
                bottomSheet.show(supportFragmentManager, "MachineInfoBottomSheet")
                true
            }

            mMap.overlays.add(marker)
        }
        mMap.invalidate()
    }

    //Comprueba si la aplicación tiene permiso para acceder a la ubicación precisa
    private fun hasLocationPermission(): Boolean {
        //Utiliza ContextCompat.checkSelfPermission para verificar si se ha concedido el permiso ACCESS_FINE_LOCATION.
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    //Solicita al usuario el permiso para acceder a la ubicación precisa si aún no está concedido.
    private fun requestLocationPermission() {

        //ActivityCompat.requestPermissions muestra un diálogo de sistema que pide el permiso ACCESS_FINE_LOCATION.
        //Usa LOCATION_PERMISSION_REQUEST para identificar la solicitud más adelante.
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST)
    }

    //Maneja la respuesta del usuario a la solicitud de permisos.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //Identifica la solicitud (se compara con LOCATION_PERMISSION_REQUEST).
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            //Si el usuario ha concedido el permiso, obtiene la ubicación.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getUserLocation()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Intenta obtener la última ubicación conocida del dispositivo.
    private fun getUserLocation() {
        //Comprueba si se ha concedido el permiso ACCESS_FINE_LOCATION.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return
        //Utiliza fusedLocationClient.lastLocation para obtener la última ubicación.
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                //Crea un GeoPoint con latitud y longitud, y llama a setupMapWithLocation() para mostrarla.
                userLocation = GeoPoint(it.latitude, it.longitude)
                setupMapWithLocation()
            } ?: run {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Crea un marcador en el mapa en la posición de la ubicación del usuario.
    private fun setupMapWithLocation() {
        userLocation?.let {
            mMap.controller.setZoom(18.0)
            mMap.controller.setCenter(it)

            val marker = Marker(mMap)
            marker.position = it
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Tu ubicación"
            mMap.overlays.add(marker)
            mMap.invalidate()
        }
    }






}