package com.eloyruiz.techassist.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.eloyruiz.techassist.R
import com.eloyruiz.techassist.data.DatabaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox

class MainTecnicoActivity : AppCompatActivity() {

    private lateinit var tvSaludo:        TextView
    private lateinit var etBuscador:      AutoCompleteTextView
    private lateinit var bannerOffline:   LinearLayout
    private lateinit var bottomNav:       BottomNavigationView

    private lateinit var cardCorrectivo:  CardView
    private lateinit var cardPreventivo:  CardView
    private lateinit var cardLimpieza:    CardView
    private lateinit var cardElectrico:   CardView
    private lateinit var cardLubricacion: CardView
    private lateinit var cardInspeccion:  CardView
    private lateinit var cardMecanico:    CardView
    private lateinit var cardPredictivo:  CardView

    private lateinit var dbHelper:    DatabaseHelper
    private var usuarioId:     Int    = -1
    private var nombreUsuario: String = "Técnico"
    private var nivelId:       Int    = 1

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback:     ConnectivityManager.NetworkCallback

    private val categorias = mapOf(
        R.id.cardCorrectivo  to "Correctivo",
        R.id.cardPreventivo  to "Preventivo",
        R.id.cardLimpieza    to "Limpieza",
        R.id.cardElectrico   to "Eléctrico",
        R.id.cardLubricacion to "Lubricación",
        R.id.cardInspeccion  to "Inspección",
        R.id.cardMecanico    to "Mecánico",
        R.id.cardPredictivo  to "Predictivo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tecnico)

        dbHelper      = DatabaseHelper(this)
        usuarioId     = intent.getIntExtra(LoginActivity.EXTRA_USUARIO_ID, -1)
        nombreUsuario = intent.getStringExtra(LoginActivity.EXTRA_USUARIO_NOMBRE) ?: "Técnico"
        nivelId       = intent.getIntExtra(LoginActivity.EXTRA_NIVEL_ID, 1)

        bindViews()
        configurarSaludo()
        configurarBuscador()
        configurarCategorias()
        configurarMenu()
    }

    override fun onResume() {
        super.onResume()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread { bannerOffline.visibility = View.GONE }
            }
            override fun onLost(network: Network) {
                runOnUiThread { bannerOffline.visibility = View.VISIBLE }
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

        val caps = connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }

    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun bindViews() {
        tvSaludo        = findViewById(R.id.tvSaludo)
        etBuscador      = findViewById(R.id.etBuscador)
        bannerOffline   = findViewById(R.id.bannerOffline)
        bottomNav       = findViewById(R.id.bottomNav)

        cardCorrectivo  = findViewById(R.id.cardCorrectivo)
        cardPreventivo  = findViewById(R.id.cardPreventivo)
        cardLimpieza    = findViewById(R.id.cardLimpieza)
        cardElectrico   = findViewById(R.id.cardElectrico)
        cardLubricacion = findViewById(R.id.cardLubricacion)
        cardInspeccion  = findViewById(R.id.cardInspeccion)
        cardMecanico    = findViewById(R.id.cardMecanico)
        cardPredictivo  = findViewById(R.id.cardPredictivo)
    }

    private fun configurarSaludo() {
        tvSaludo.text = "Hola, $nombreUsuario 👋"
    }

    private fun configurarBuscador() {
        val sugerencias = listOf(
            "Correctivo", "Preventivo", "Limpieza", "Eléctrico",
            "Lubricación", "Inspección", "Mecánico", "Predictivo"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sugerencias)
        etBuscador.setAdapter(adapter)

        etBuscador.setOnItemClickListener { _, _, position, _ ->
            buscarRecomendacion(sugerencias[position])
            etBuscador.dismissDropDown()
        }

        etBuscador.setOnEditorActionListener { _, _, _ ->
            val texto = etBuscador.text?.toString()?.trim() ?: ""
            if (texto.isNotEmpty()) buscarRecomendacion(texto)
            true
        }
    }

    private fun configurarCategorias() {
        listOf(
            cardCorrectivo, cardPreventivo, cardLimpieza, cardElectrico,
            cardLubricacion, cardInspeccion, cardMecanico, cardPredictivo
        ).forEach { card ->
            card.setOnClickListener {
                val categoria = categorias[card.id] ?: return@setOnClickListener
                buscarRecomendacion(categoria)
            }
        }
    }

    private fun configurarMenu() {
        val rol = intent.getStringExtra(LoginActivity.EXTRA_USUARIO_ROL) ?: ""
        bottomNav.menu.findItem(R.id.nav_panel)?.isVisible =
            rol == "Supervisor" || rol == "Administrador"

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio    -> true
                R.id.nav_historial -> { mostrarHistorial(); false }
                R.id.nav_perfil    -> { mostrarPerfil(); false }
                R.id.nav_panel     -> {
                    startActivity(
                        Intent(this, SupervisorActivity::class.java).apply {
                            putExtra(LoginActivity.EXTRA_USUARIO_NOMBRE, nombreUsuario)
                            putExtra(LoginActivity.EXTRA_USUARIO_ROL, rol)
                        }
                    )
                    false
                }
                else -> false
            }
        }
    }

    private fun mostrarHistorial() {
        val consultas  = dbHelper.getConsultasByUsuario(usuarioId)
        val dialog     = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_historial, null)

        val tvContenido = dialogView.findViewById<TextView>(R.id.tvHistorialContenido)
        val btnCerrar   = dialogView.findViewById<MaterialButton>(R.id.btnCerrarHistorial)

        if (consultas.isEmpty()) {
            tvContenido.text = "Aún no has realizado ninguna consulta."
        } else {
            tvContenido.text = consultas.joinToString("\n\n") { consulta ->
                val categoria = consulta["categoria_tarea"] as? String ?: "—"
                val fecha     = consulta["fecha"]           as? String ?: "—"
                "📅 $fecha  ·  $categoria"
            }
        }

        btnCerrar.setOnClickListener { dialog.dismiss() }
        dialog.setContentView(dialogView)
        dialog.show()
    }

    private fun mostrarPerfil() {
        val nivel = when (nivelId) {
            1    -> "Básico"
            2    -> "Intermedio"
            else -> "Avanzado"
        }

        val dialog     = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_perfil, null)

        dialogView.findViewById<TextView>(R.id.tvPerfilNombre).text = nombreUsuario
        dialogView.findViewById<TextView>(R.id.tvPerfilNivel).text  = "Nivel Digital: $nivel"
        dialogView.findViewById<TextView>(R.id.tvPerfilId).text     = "ID: $usuarioId"

        val btnCerrar = dialogView.findViewById<MaterialButton>(R.id.btnCerrarPerfil)
        btnCerrar.setOnClickListener { dialog.dismiss() }

        dialog.setContentView(dialogView)
        dialog.show()
    }

    private fun buscarRecomendacion(categoriaTarea: String) {
        val recomendacion = dbHelper.getRecomendacionPorCategoria(categoriaTarea)

        if (recomendacion == null) {
            mostrarDialogoRecomendacion(
                categoria   = categoriaTarea,
                herramienta = "Sin recomendación",
                descripcion = "",
                explicacion = "No hay ninguna regla definida para esta categoría. Consulta con tu supervisor.",
                paso1       = "—", paso2 = "—", paso3 = "—"
            )
            return
        }

        val herramientaId     = recomendacion["herramienta_id"]          as? Int    ?: return
        val herramientaNombre = recomendacion["herramienta_nombre"]       as? String ?: ""
        val herramientaDesc   = recomendacion["herramienta_descripcion"]  as? String ?: ""
        val explicacion       = recomendacion["explicacion"]              as? String ?: ""
        val guia              = dbHelper.getGuiaByHerramientaId(herramientaId)

        dbHelper.insertConsulta(usuarioId, herramientaId, categoriaTarea)

        mostrarDialogoRecomendacion(
            categoria   = categoriaTarea,
            herramienta = herramientaNombre,
            descripcion = herramientaDesc,
            explicacion = explicacion,
            paso1       = guia?.get("paso_1") as? String ?: "Consulta el manual de la herramienta.",
            paso2       = guia?.get("paso_2") as? String ?: "Verifica el estado del equipo.",
            paso3       = guia?.get("paso_3") as? String ?: "Registra el resultado en el sistema."
        )
    }

    private fun mostrarDialogoRecomendacion(
        categoria: String, herramienta: String, descripcion: String,
        explicacion: String, paso1: String, paso2: String, paso3: String
    ) {
        val dialog     = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_recomendacion, null)

        dialogView.findViewById<TextView>(R.id.tvCategoria).text              = categoria
        dialogView.findViewById<TextView>(R.id.tvHerramienta).text            = herramienta
        dialogView.findViewById<TextView>(R.id.tvDescripcionHerramienta).text = descripcion
        dialogView.findViewById<TextView>(R.id.tvExplicacion).text            = explicacion
        dialogView.findViewById<TextView>(R.id.tvPaso1).text                  = paso1
        dialogView.findViewById<TextView>(R.id.tvPaso2).text                  = paso2
        dialogView.findViewById<TextView>(R.id.tvPaso3).text                  = paso3

        val checkPaso1 = dialogView.findViewById<MaterialCheckBox>(R.id.checkPaso1)
        val checkPaso2 = dialogView.findViewById<MaterialCheckBox>(R.id.checkPaso2)
        val checkPaso3 = dialogView.findViewById<MaterialCheckBox>(R.id.checkPaso3)
        val tvPaso2    = dialogView.findViewById<TextView>(R.id.tvPaso2)
        val tvPaso3    = dialogView.findViewById<TextView>(R.id.tvPaso3)
        val btnCerrar  = dialogView.findViewById<MaterialButton>(R.id.btnCerrar)

        btnCerrar.isEnabled  = false;  btnCerrar.alpha  = 0.4f
        checkPaso2.isEnabled = false;  checkPaso2.alpha = 0.4f;  tvPaso2.alpha = 0.4f
        checkPaso3.isEnabled = false;  checkPaso3.alpha = 0.4f;  tvPaso3.alpha = 0.4f

        checkPaso1.setOnCheckedChangeListener { _, checked ->
            checkPaso2.isEnabled = checked
            checkPaso2.alpha     = if (checked) 1f else 0.4f
            tvPaso2.alpha        = if (checked) 1f else 0.4f
            if (!checked) {
                checkPaso2.isChecked = false
                checkPaso3.isChecked = false
                checkPaso3.isEnabled = false;  checkPaso3.alpha = 0.4f;  tvPaso3.alpha = 0.4f
                btnCerrar.isEnabled  = false;  btnCerrar.alpha  = 0.4f
            }
        }

        checkPaso2.setOnCheckedChangeListener { _, checked ->
            checkPaso3.isEnabled = checked
            checkPaso3.alpha     = if (checked) 1f else 0.4f
            tvPaso3.alpha        = if (checked) 1f else 0.4f
            if (!checked) {
                checkPaso3.isChecked = false
                btnCerrar.isEnabled  = false;  btnCerrar.alpha  = 0.4f
            }
        }

        checkPaso3.setOnCheckedChangeListener { _, checked ->
            btnCerrar.isEnabled = checked
            btnCerrar.alpha     = if (checked) 1f else 0.4f
        }

        btnCerrar.setOnClickListener { dialog.dismiss() }

        dialog.setContentView(dialogView)
        dialog.show()
    }
}