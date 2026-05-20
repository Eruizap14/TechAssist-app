package com.eloyruiz.techassist.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eloyruiz.techassist.R
import com.eloyruiz.techassist.data.DatabaseHelper
import com.eloyruiz.techassist.data.TechAssistContract.NivelDigital
import com.eloyruiz.techassist.data.TechAssistContract.Usuario
import com.google.android.material.button.MaterialButton

class SupervisorActivity : AppCompatActivity() {

    private lateinit var tvSaludoSupervisor: TextView
    private lateinit var tvTotalConsultas:   TextView
    private lateinit var tvHerramientaTop:   TextView
    private lateinit var rvTecnicos:         RecyclerView
    private lateinit var btnExportar:        MaterialButton
    private lateinit var bannerOffline:      LinearLayout
    private lateinit var btnVolverInicio:    ImageButton

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter:  TecnicoAdapter
    private var nombreSupervisor = "Supervisor"

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback:     ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisor)

        dbHelper         = DatabaseHelper(this)
        nombreSupervisor = intent.getStringExtra(LoginActivity.EXTRA_USUARIO_NOMBRE) ?: "Supervisor"

        bindViews()
        configurarToolbar()
        configurarRecyclerView()
        cargarDatos()
        configurarBotonExportar()
        configurarBotonVolver()
    }

    override fun onResume() {
        super.onResume()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    bannerOffline.visibility = View.GONE
                    actualizarEstadoBoton(true)
                }
            }
            override fun onLost(network: Network) {
                runOnUiThread {
                    bannerOffline.visibility = View.VISIBLE
                    actualizarEstadoBoton(false)
                }
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)

        val online = connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        bannerOffline.visibility = if (online) View.GONE else View.VISIBLE
        actualizarEstadoBoton(online)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun bindViews() {
        tvSaludoSupervisor = findViewById(R.id.tvSaludoSupervisor)
        tvTotalConsultas   = findViewById(R.id.tvTotalConsultas)
        tvHerramientaTop   = findViewById(R.id.tvHerramientaTop)
        rvTecnicos         = findViewById(R.id.rvTecnicos)
        btnExportar        = findViewById(R.id.btnExportar)
        bannerOffline      = findViewById(R.id.bannerOffline)
        btnVolverInicio    = findViewById(R.id.btnVolverInicio)
    }

    private fun configurarToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tvSaludoSupervisor.text = nombreSupervisor
    }

    private fun configurarRecyclerView() {
        adapter = TecnicoAdapter(emptyList())
        rvTecnicos.layoutManager = LinearLayoutManager(this)
        rvTecnicos.adapter       = adapter
        rvTecnicos.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    private fun cargarDatos() {
        val tecnicos = dbHelper.getUsuariosByRol("Técnico")

        val niveles = dbHelper.getAllNivelesDigitales()
            .associate { (it[NivelDigital.COL_ID] as Int) to (it[NivelDigital.COL_NOMBRE] as String) }

        val todasConsultas = dbHelper.getAllConsultas()

        val items = tecnicos.map { tecnico ->
            val id           = tecnico[Usuario.COL_ID]               as Int
            val nombre       = tecnico[Usuario.COL_NOMBRE]           as String
            val nivelId      = tecnico[Usuario.COL_NIVEL_DIGITAL_ID] as Int
            val nivel        = niveles[nivelId] ?: "—"
            val numConsultas = todasConsultas.count { it["usuario_id"] == id }
            TecnicoItem(nombre = nombre, nivel = nivel, consultas = numConsultas)
        }

        adapter.actualizar(items)

        val hoy = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        val consultasHoy = todasConsultas.count { it["fecha"] == hoy }
        tvTotalConsultas.text = consultasHoy.toString()

        val herramientasTop = dbHelper.getHerramientasMasConsultadas(1)
        tvHerramientaTop.text = if (herramientasTop.isNotEmpty())
            herramientasTop[0]["nombre"] as? String ?: "—"
        else "—"
    }

    private fun configurarBotonExportar() {
        btnExportar.setOnClickListener {
            val cm      = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork
            val caps    = network?.let { cm.getNetworkCapabilities(it) }
            val online  = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

            if (!online) {
                Toast.makeText(this, "Exportación no disponible sin conexión", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Enviando reporte…", Toast.LENGTH_SHORT).show()
            exportarReporte()
        }
    }

    private fun actualizarEstadoBoton(online: Boolean) {
        if (online) {
            btnExportar.isEnabled = true
            btnExportar.text      = "📤  Exportar Reporte"
            btnExportar.setBackgroundColor(getColor(R.color.color_primary))
        } else {
            btnExportar.isEnabled = false
            btnExportar.text      = "Exportación no disponible"
            btnExportar.setBackgroundColor(0xFFBDBDBD.toInt())
        }
    }

    private fun exportarReporte() {
        val tecnicos     = dbHelper.getUsuariosByRol("Técnico")
        val consultas    = dbHelper.getAllConsultas()
        val herramientas = dbHelper.getHerramientasMasConsultadas(5)

        val json = buildString {
            append("{")
            append("\"fecha\":\"${java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())}\",")
            append("\"total_tecnicos\":${tecnicos.size},")
            append("\"total_consultas\":${consultas.size},")
            append("\"herramientas_top\":[")
            herramientas.forEachIndexed { i, h ->
                append("{\"nombre\":\"${h["nombre"]}\",\"consultas\":${h["total_consultas"]}}")
                if (i < herramientas.size - 1) append(",")
            }
            append("]}")
        }

        Toast.makeText(this, "Reporte preparado ✓", Toast.LENGTH_SHORT).show()
    }

    private fun configurarBotonVolver() {
        val rol = intent.getStringExtra(LoginActivity.EXTRA_USUARIO_ROL) ?: ""
        if (rol == "Administrador") {
            btnVolverInicio.visibility = View.VISIBLE
            btnVolverInicio.setOnClickListener { finish() }
        }
    }
}