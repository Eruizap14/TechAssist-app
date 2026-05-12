package com.eloyruiz.techassist.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.eloyruiz.techassist.R
import com.eloyruiz.techassist.data.DatabaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class MainTecnicoActivity : AppCompatActivity() {

    // ── Vistas ────────────────────────────────────────────────────────────────
    private lateinit var tvSaludo:        TextView
    private lateinit var etBuscador: AutoCompleteTextView
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

    // ── Datos ─────────────────────────────────────────────────────────────────
    private lateinit var dbHelper:    DatabaseHelper
    private var usuarioId:     Int    = -1
    private var nombreUsuario: String = "Técnico"
    private var nivelId:       Int    = 1

    // ── Mapa cardView.id → categoria_tarea de la BD ───────────────────────────
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

    // ─────────────────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tecnico)

        dbHelper      = DatabaseHelper(this)
        usuarioId     = intent.getIntExtra(LoginActivity.EXTRA_USUARIO_ID, -1)
        nombreUsuario = intent.getStringExtra(LoginActivity.EXTRA_USUARIO_NOMBRE) ?: "Técnico"
        nivelId       = intent.getIntExtra(LoginActivity.EXTRA_NIVEL_ID, 1)

        bindViews()
        configurarToolbar()
        configurarSaludo()
        configurarBuscador()
        configurarCategorias()
        configurarMenu()
        comprobarConexion()
    }

    // ── Binding ───────────────────────────────────────────────────────────────

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

    // ── Toolbar ───────────────────────────────────────────────────────────────

    private fun configurarToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    // ── Saludo personalizado con nombre real del técnico ──────────────────────

    private fun configurarSaludo() {
        tvSaludo.text = "Hola, $nombreUsuario 👋"
    }

    // ── Buscador ──────────────────────────────────────────────────────────────

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

    // ── Rejilla de categorías ─────────────────────────────────────────────────

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

    // ── Menú inferior ─────────────────────────────────────────────────────────

    private fun configurarMenu() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio   -> true  // ya estamos aquí
                R.id.nav_historial -> {
                    // TODO: abrir pantalla de historial de consultas
                    true
                }
                R.id.nav_perfil   -> {
                    // TODO: abrir perfil del técnico
                    true
                }
                else -> false
            }
        }
    }

    // ── Lógica de recomendación ───────────────────────────────────────────────

    private fun buscarRecomendacion(categoriaTarea: String) {
        val recomendacion = dbHelper.getRecomendacionPorCategoria(categoriaTarea)

        if (recomendacion == null) {
            mostrarDialogoRecomendacion(
                categoria   = categoriaTarea,
                herramienta = "Sin recomendación",
                descripcion = "",
                explicacion = "No hay ninguna regla definida para esta categoría. Consulta con tu supervisor.",
                paso1       = "—",
                paso2       = "—",
                paso3       = "—"
            )
            return
        }

        val herramientaId    = recomendacion["herramienta_id"] as? Int ?: return
        val herramientaNombre = recomendacion["herramienta_nombre"] as? String ?: ""
        val herramientaDesc  = recomendacion["herramienta_descripcion"] as? String ?: ""
        val explicacion      = recomendacion["explicacion"] as? String ?: ""
        val guia             = dbHelper.getGuiaByHerramientaId(herramientaId)

        // Registrar consulta en la BD — alimenta el panel del supervisor
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

    // ── Bottom Sheet de recomendación (HU-01, HU-02, HU-03) ──────────────────

    private fun mostrarDialogoRecomendacion(
        categoria:   String,
        herramienta: String,
        descripcion: String,
        explicacion: String,
        paso1:       String,
        paso2:       String,
        paso3:       String
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

        dialogView.findViewById<MaterialButton>(R.id.btnCerrar).setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(dialogView)
        dialog.show()
    }

    // ── Detección de conexión ─────────────────────────────────────────────────

    private fun comprobarConexion() {
        bannerOffline.visibility = if (isOnline()) View.GONE else View.VISIBLE
    }

    private fun isOnline(): Boolean {
        val cm      = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps    = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}