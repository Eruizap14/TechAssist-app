package com.eloyruiz.techassist.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eloyruiz.techassist.R
import com.eloyruiz.techassist.data.DatabaseHelper
import com.eloyruiz.techassist.data.TechAssistContract.Usuario
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView // Import corregido

class MinitestActivity : AppCompatActivity() {

    // ── Vistas ────────────────────────────────────────────────────────────────
    private lateinit var progressBar:  ProgressBar
    private lateinit var tvContador:   TextView
    private lateinit var tvPuntuacion: TextView
    private lateinit var tvPregunta:   TextView

    // Cambio de CardView a MaterialCardView para habilitar strokes
    private lateinit var cardOpcion1: MaterialCardView
    private lateinit var cardOpcion2: MaterialCardView
    private lateinit var cardOpcion3: MaterialCardView
    private lateinit var tvOpcion1:   TextView
    private lateinit var tvOpcion2:   TextView
    private lateinit var tvOpcion3:   TextView

    private lateinit var btnVolver:   MaterialButton
    private lateinit var btnContinuar: MaterialButton

    // ── Estado ────────────────────────────────────────────────────────────────
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var prefs:    SharedPreferences

    private var preguntaActual  = 0
    private var puntuacion      = 0
    private var opcionSeleccionada: Int? = null
    private var usuarioId       = -1

    // ── Preguntas ─────────────────────────────────────────────────────────────
    data class Pregunta(
        val enunciado: String,
        val opciones:  List<String>,
        val puntos:    List<Int>
    )

    private val preguntas = listOf(
        Pregunta(
            enunciado = "¿Has utilizado alguna vez una tablet o smartphone para consultar información técnica en el trabajo?",
            opciones  = listOf("Nunca lo he usado", "Sí, alguna vez", "Sí, lo uso habitualmente"),
            puntos    = listOf(0, 1, 2)
        ),
        Pregunta(
            enunciado = "Si tienes que buscar información sobre una avería, ¿qué haces normalmente?",
            opciones  = listOf("Pregunto a un compañero", "Busco en papel o manual físico", "Busco en internet o una app"),
            puntos    = listOf(0, 1, 2)
        ),
        Pregunta(
            enunciado = "¿Sabes lo que es una aplicación móvil y cómo instalarla?",
            opciones  = listOf("No, no sé cómo funciona", "He visto cómo se hace pero no solo", "Sí, lo hago sin problema"),
            puntos    = listOf(0, 1, 2)
        ),
        Pregunta(
            enunciado = "¿Has utilizado alguna vez un software de gestión de mantenimiento (CMMS, SAP, etc.)?",
            opciones  = listOf("Nunca", "He visto alguna demo o formación básica", "Sí, lo uso en el trabajo"),
            puntos    = listOf(0, 1, 2)
        ),
        Pregunta(
            enunciado = "Si una app te muestra un error o no carga, ¿qué sueles hacer?",
            opciones  = listOf("Llamo a alguien para que me ayude", "Cierro y vuelvo a abrir la app", "Intento diagnosticar el problema yo mismo"),
            puntos    = listOf(0, 1, 2)
        ),
        Pregunta(
            enunciado = "¿Con qué frecuencia utilizas herramientas digitales en tu jornada laboral?",
            opciones  = listOf("Casi nunca", "Ocasionalmente", "A diario, son parte de mi trabajo"),
            puntos    = listOf(0, 1, 2)
        )
    )

    private val puntosPorPregunta = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minitest)

        dbHelper  = DatabaseHelper(this)
        prefs     = getSharedPreferences("techassist_prefs", MODE_PRIVATE)
        usuarioId = intent.getIntExtra(LoginActivity.EXTRA_USUARIO_ID, -1)

        bindViews()
        mostrarPregunta(preguntaActual)
        configurarBotones()
    }

    private fun bindViews() {
        progressBar   = findViewById(R.id.progressBar)
        tvContador    = findViewById(R.id.tvContador)
        tvPuntuacion  = findViewById(R.id.tvPuntuacion)
        tvPregunta    = findViewById(R.id.tvPregunta)

        cardOpcion1   = findViewById(R.id.cardOpcion1)
        cardOpcion2   = findViewById(R.id.cardOpcion2)
        cardOpcion3   = findViewById(R.id.cardOpcion3)
        tvOpcion1     = findViewById(R.id.tvOpcion1)
        tvOpcion2     = findViewById(R.id.tvOpcion2)
        tvOpcion3     = findViewById(R.id.tvOpcion3)

        btnVolver     = findViewById(R.id.btnVolver)
        btnContinuar  = findViewById(R.id.btnContinuar)
    }

    private fun mostrarPregunta(index: Int) {
        val pregunta = preguntas[index]
        val total    = preguntas.size
        val pct      = (index.toFloat() / total * 100).toInt()

        progressBar.progress  = pct
        tvContador.text       = "$pct% completado"
        tvPuntuacion.text     = "$puntuacion/${total * 2}"
        tvPregunta.text       = pregunta.enunciado

        tvOpcion1.text = pregunta.opciones[0]
        tvOpcion2.text = pregunta.opciones[1]
        tvOpcion3.text = pregunta.opciones[2]

        opcionSeleccionada = null
        resetCards()

        if (index < puntosPorPregunta.size) {
            val puntosGuardados = puntosPorPregunta[index]
            val opcionIdx = pregunta.puntos.indexOf(puntosGuardados)
            if (opcionIdx != -1) seleccionarCard(opcionIdx)
        }

        val cards = listOf(cardOpcion1, cardOpcion2, cardOpcion3)
        cards.forEachIndexed { i, card ->
            card.setOnClickListener { seleccionarCard(i) }
        }

        btnVolver.isEnabled = index > 0
        btnVolver.alpha = if (index > 0) 1f else 0.4f
    }

    private fun seleccionarCard(index: Int) {
        opcionSeleccionada = index
        resetCards()

        val cards = listOf(cardOpcion1, cardOpcion2, cardOpcion3)
        val tvs   = listOf(tvOpcion1,   tvOpcion2,   tvOpcion3)

        cards[index].setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_success_light))
        cards[index].strokeColor = ContextCompat.getColor(this, R.color.color_success)
        cards[index].strokeWidth = 6
        tvs[index].setTextColor(ContextCompat.getColor(this, R.color.color_on_background))

        cards.forEachIndexed { i, card ->
            if (i != index) {
                card.strokeColor = ContextCompat.getColor(this, R.color.color_primary)
                card.strokeWidth = 4
            }
        }
    }

    private fun resetCards() {
        val cards = listOf(cardOpcion1, cardOpcion2, cardOpcion3)
        cards.forEach { card ->
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_surface))
            card.strokeColor = ContextCompat.getColor(this, R.color.color_on_surface_secondary)
            card.strokeWidth = 2
        }
        listOf(tvOpcion1, tvOpcion2, tvOpcion3).forEach {
            it.setTextColor(ContextCompat.getColor(this, R.color.color_on_background))
        }
    }

    private fun configurarBotones() {
        btnVolver.setOnClickListener {
            if (preguntaActual > 0) {
                if (preguntaActual < puntosPorPregunta.size) {
                    puntosPorPregunta.removeAt(preguntaActual)
                }
                if (preguntaActual - 1 < puntosPorPregunta.size) {
                    puntuacion -= puntosPorPregunta[preguntaActual - 1]
                    puntosPorPregunta.removeAt(preguntaActual - 1)
                }
                preguntaActual--
                mostrarPregunta(preguntaActual)
            }
        }

        btnContinuar.setOnClickListener {
            val seleccion = opcionSeleccionada
            if (seleccion == null) {
                tvPregunta.setTextColor(ContextCompat.getColor(this, R.color.color_primary))
                tvPregunta.postDelayed({
                    tvPregunta.setTextColor(ContextCompat.getColor(this, R.color.color_on_background))
                }, 600)
                return@setOnClickListener
            }

            val puntosObtenidos = preguntas[preguntaActual].puntos[seleccion]

            if (preguntaActual < puntosPorPregunta.size) {
                puntuacion -= puntosPorPregunta[preguntaActual]
                puntosPorPregunta[preguntaActual] = puntosObtenidos
            } else {
                puntosPorPregunta.add(puntosObtenidos)
            }
            puntuacion += puntosObtenidos
            preguntaActual++

            if (preguntaActual < preguntas.size) {
                mostrarPregunta(preguntaActual)
            } else {
                finalizarTest()
            }
        }
    }

    private fun finalizarTest() {
        val nivel = calcularNivel(puntuacion)
        val nivelId = when (nivel) {
            "Básico"     -> 1
            "Intermedio" -> 2
            else         -> 3
        }

        val usuarioData = dbHelper.getUsuarioById(usuarioId)
        val nombre      = usuarioData?.get(Usuario.COL_NOMBRE) as? String ?: ""
        val rol         = usuarioData?.get(Usuario.COL_ROL)    as? String ?: ""

        dbHelper.updateUsuario(
            id             = usuarioId,
            nombre         = nombre,
            rol            = rol,
            nivelDigitalId = nivelId
        )

        prefs.edit()
            .putBoolean(LoginActivity.miniTestKey(usuarioId), true)
            .apply()

        startActivity(
            Intent(this, ResultadoMinitestActivity::class.java).apply {
                putExtra(EXTRA_NIVEL,                        nivel)
                putExtra(EXTRA_NIVEL_ID,                     nivelId)
                putExtra(LoginActivity.EXTRA_USUARIO_ID,     usuarioId)
                putExtra(LoginActivity.EXTRA_USUARIO_NOMBRE, nombre)
                putExtra(LoginActivity.EXTRA_USUARIO_ROL,    rol)
            }
        )
        finish()
    }

    private fun calcularNivel(puntos: Int) = when {
        puntos <= 4 -> "Básico"
        puntos <= 8 -> "Intermedio"
        else        -> "Avanzado"
    }

    companion object {
        const val EXTRA_NIVEL    = "extra_nivel"
        const val EXTRA_NIVEL_ID = "extra_nivel_id"
    }
}