package com.eloyruiz.techassist.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
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

    // ── Vistas ────────────────────────────────────────────────────────────────
    private lateinit var tvSaludoSupervisor: TextView
    private lateinit var tvTotalConsultas:   TextView
    private lateinit var tvHerramientaTop:   TextView
    private lateinit var rvTecnicos:         RecyclerView
    private lateinit var btnExportar:        MaterialButton
    private lateinit var bannerOffline:      LinearLayout

    // ── Datos ─────────────────────────────────────────────────────────────────
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter:  TecnicoAdapter
    private var nombreSupervisor = "Supervisor"

    // ─────────────────────────────────────────────────────────────────────────

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
        comprobarConexion()
    }

    // ── Binding ───────────────────────────────────────────────────────────────

    private fun bindViews() {
        tvSaludoSupervisor = findViewById(R.id.tvSaludoSupervisor)
        tvTotalConsultas   = findViewById(R.id.tvTotalConsultas)
        tvHerramientaTop   = findViewById(R.id.tvHerramientaTop)
        rvTecnicos         = findViewById(R.id.rvTecnicos)
        btnExportar        = findViewById(R.id.btnExportar)
        bannerOffline      = findViewById(R.id.bannerOffline)
    }

    // ── Toolbar ───────────────────────────────────────────────────────────────

    private fun configurarToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tvSaludoSupervisor.text = nombreSupervisor
    }

    // ── RecyclerView ──────────────────────────────────────────────────────────

    private fun configurarRecyclerView() {
        adapter = TecnicoAdapter(emptyList())
        rvTecnicos.layoutManager = LinearLayoutManager(this)
        rvTecnicos.adapter       = adapter
        rvTecnicos.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    // ── Carga de datos desde la BD ────────────────────────────────────────────

    private fun cargarDatos() {
        // Todos los usuarios con rol Técnico
        val tecnicos = dbHelper.getUsuariosByRol("Técnico")

        // Niveles digitales para traducir el ID al nombre
        val niveles = dbHelper.getAllNivelesDigitales()
            .associate { (it[NivelDigital.COL_ID] as Int) to (it[NivelDigital.COL_NOMBRE] as String) }

        // Todas las consultas para calcular cuántas hizo cada técnico
        val todasConsultas = dbHelper.getAllConsultas()

        // Construir lista de items para el adapter
        val items = tecnicos.map { tecnico ->
            val id     = tecnico[Usuario.COL_ID]               as Int
            val nombre = tecnico[Usuario.COL_NOMBRE]           as String
            val nivelId= tecnico[Usuario.COL_NIVEL_DIGITAL_ID] as Int
            val nivel  = niveles[nivelId] ?: "—"
            val numConsultas = todasConsultas.count { it["usuario_id"] == id }

            TecnicoItem(nombre = nombre, nivel = nivel, consultas = numConsultas)
        }

        adapter.actualizar(items)

        // ── KPIs ──────────────────────────────────────────────────────────────

        // Total de consultas de hoy
        val hoy = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        val consultasHoy = todasConsultas.count { it["fecha"] == hoy }
        tvTotalConsultas.text = consultasHoy.toString()

        // Herramienta más consultada (top 1)
        val herramientasTop = dbHelper.getHerramientasMasConsultadas(1)
        tvHerramientaTop.text = if (herramientasTop.isNotEmpty())
            herramientasTop[0]["nombre"] as? String ?: "—"
        else "—"
    }

    // ── Botón exportar — se desactiva en gris sin conexión — según 5.2.6 ─────

    private fun configurarBotonExportar() {
        actualizarEstadoBoton(isOnline())

        btnExportar.setOnClickListener {
            if (!isOnline()) {
                Toast.makeText(this, "Exportación no disponible sin conexión", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: disparar Webhook n8n con los datos del reporte
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
            // Gris desactivado #BDBDBD — según 5.2.6
            btnExportar.isEnabled = false
            btnExportar.text      = "Exportación no disponible"
            btnExportar.setBackgroundColor(0xFFBDBDBD.toInt())
        }
    }

    // ── Exportar (stub — pendiente de integrar n8n) ───────────────────────────

    private fun exportarReporte() {
        // Recoger datos para el reporte
        val tecnicos     = dbHelper.getUsuariosByRol("Técnico")
        val consultas    = dbHelper.getAllConsultas()
        val herramientas = dbHelper.getHerramientasMasConsultadas(5)

        // Construir JSON básico del reporte
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

        // TODO: enviar `json` al webhook de n8n
        // val url = "https://tu-instancia-n8n.com/webhook/techassist"
        // Aquí iría la llamada HTTP con Retrofit o similar

        Toast.makeText(this, "Reporte preparado ✓", Toast.LENGTH_SHORT).show()
    }

    // ── Conexión ──────────────────────────────────────────────────────────────

    private fun comprobarConexion() {
        val online = isOnline()
        bannerOffline.visibility = if (online) View.GONE else View.VISIBLE
        actualizarEstadoBoton(online)
    }

    private fun isOnline(): Boolean {
        val cm      = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps    = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}