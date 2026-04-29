package com.eloyruiz.techassist.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eloyruiz.techassist.R
import com.eloyruiz.techassist.data.DatabaseHelper
import com.eloyruiz.techassist.data.TechAssistContract.Usuario
import com.google.android.material.button.MaterialButton

class MinitestActivity : AppCompatActivity() {

    // ── Vistas ────────────────────────────────────────────────────────────────
    private lateinit var progressBar: ProgressBar
    private lateinit var tvContador: TextView
    private lateinit var tvPregunta: TextView
    private lateinit var btnOpcion1: MaterialButton
    private lateinit var btnOpcion2: MaterialButton
    private lateinit var btnOpcion3: MaterialButton

    // ── Datos ─────────────────────────────────────────────────────────────────
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var prefs: SharedPreferences

    private var preguntaActual = 0
    private var puntuacion     = 0
    private var usuarioId      = -1

    // ── Preguntas ─────────────────────────────────────────────────────────────
    // Cada pregunta tiene: enunciado + 3 opciones + índice de la respuesta con más puntos (0,1,2)
    // Puntuación: opción básica = 0, intermedia = 1, avanzada = 2
    data class Pregunta(
        val enunciado: String,
        val opciones: List<String>,
        val puntos: List<Int>          // puntos para cada opción (0, 1 o 2)
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
            opciones  = listOf("No, no sé cómo funciona", "He visto cómo se hace pero no lo hago solo", "Sí, lo hago sin problema"),
            puntos    = listOf(0, 1, 2)
        ),
        Pregunta(
            enunciado = "¿Has utilizado alguna vez un programa o software de gestión de mantenimiento (CMMS, SAP, etc.)?",
            opciones  = listOf("Nunca", "He visto alguna demo o formación básica", "Sí, lo uso o lo he usado en el trabajo"),
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

    // ─────────────────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minitest)

        dbHelper  = DatabaseHelper(this)
        prefs     = getSharedPreferences("techassist_prefs", MODE_PRIVATE)
        usuarioId = intent.getIntExtra(LoginActivity.EXTRA_USUARIO_ID, -1)

        bindViews()
        mostrarPregunta(preguntaActual)
    }

    // ── Binding ───────────────────────────────────────────────────────────────

    private fun bindViews() {
        progressBar = findViewById(R.id.progressBar)
        tvContador  = findViewById(R.id.tvContador)
        tvPregunta  = findViewById(R.id.tvPregunta)
        btnOpcion1  = findViewById(R.id.btnOpcion1)
        btnOpcion2  = findViewById(R.id.btnOpcion2)
        btnOpcion3  = findViewById(R.id.btnOpcion3)
    }

    // ── Lógica del test ───────────────────────────────────────────────────────

    private fun mostrarPregunta(index: Int) {
        val pregunta = preguntas[index]
        val total    = preguntas.size

        // Progreso
        progressBar.progress = ((index.toFloat() / total) * 100).toInt()
        tvContador.text      = "Pregunta ${index + 1} de $total"

        // Enunciado
        tvPregunta.text = pregunta.enunciado

        // Opciones — resetear estilo primero
        val botones = listOf(btnOpcion1, btnOpcion2, btnOpcion3)
        botones.forEachIndexed { i, btn ->
            btn.text = pregunta.opciones[i]
            resetBoton(btn)
            btn.setOnClickListener {
                seleccionarOpcion(btn, botones, pregunta.puntos[i])
            }
        }
    }

    private fun seleccionarOpcion(
        seleccionado: MaterialButton,
        todos: List<MaterialButton>,
        puntosObtenidos: Int
    ) {
        // Marcar visualmente la opción seleccionada
        todos.forEach { resetBoton(it) }
        marcarBoton(seleccionado)

        // Acumular puntos y avanzar tras un breve delay
        puntuacion += puntosObtenidos
        preguntaActual++

        seleccionado.postDelayed({
            if (preguntaActual < preguntas.size) {
                mostrarPregunta(preguntaActual)
            } else {
                finalizarTest()
            }
        }, 350)
    }

    private fun finalizarTest() {
        // Calcular nivel según puntuación total
        // Máximo posible: 6 preguntas × 2 puntos = 12
        val nivel = calcularNivel(puntuacion)

        // Actualizar nivel digital del usuario en la BD
        val nivelId = when (nivel) {
            "Básico"      -> 1
            "Intermedio"  -> 2
            else          -> 3   // Avanzado
        }
        dbHelper.updateUsuario(
            id            = usuarioId,
            nombre        = dbHelper.getUsuarioById(usuarioId)?.get(Usuario.COL_NOMBRE) as? String ?: "",
            rol           = dbHelper.getUsuarioById(usuarioId)?.get(Usuario.COL_ROL)    as? String ?: "",
            nivelDigitalId = nivelId
        )

        // Marcar minitest como completado para este usuario
        prefs.edit()
            .putBoolean(LoginActivity.miniTestKey(usuarioId), true)
            .apply()

        // Navegar a la pantalla de resultado
        val intent = Intent(this, ResultadoMinitestActivity::class.java).apply {
            putExtra(EXTRA_NIVEL,       nivel)
            putExtra(EXTRA_NIVEL_ID,    nivelId)
            putExtra(LoginActivity.EXTRA_USUARIO_ID,     usuarioId)
            putExtra(LoginActivity.EXTRA_USUARIO_NOMBRE,
                dbHelper.getUsuarioById(usuarioId)?.get(Usuario.COL_NOMBRE) as? String ?: "")
            putExtra(LoginActivity.EXTRA_USUARIO_ROL,
                dbHelper.getUsuarioById(usuarioId)?.get(Usuario.COL_ROL)    as? String ?: "")
        }
        startActivity(intent)
        finish()
    }

    private fun calcularNivel(puntos: Int): String {
        // 0-4  → Básico
        // 5-8  → Intermedio
        // 9-12 → Avanzado
        return when {
            puntos <= 4  -> "Básico"
            puntos <= 8  -> "Intermedio"
            else         -> "Avanzado"
        }
    }

    // ── Estilo de botones ─────────────────────────────────────────────────────

    private fun resetBoton(btn: MaterialButton) {
        btn.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btn.strokeColor = ContextCompat.getColorStateList(this, R.color.color_on_surface_secondary)
        btn.strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_default)   // 1dp
        btn.setTextColor(ContextCompat.getColor(this, R.color.color_on_background))
        btn.typeface = Typeface.DEFAULT
    }

    private fun marcarBoton(btn: MaterialButton) {
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.color_primary))
        btn.strokeColor = ContextCompat.getColorStateList(this, R.color.color_primary)
        btn.setTextColor(ContextCompat.getColor(this, R.color.color_on_primary))
        btn.typeface = Typeface.DEFAULT_BOLD
    }

    // ── Companion ─────────────────────────────────────────────────────────────

    companion object {
        const val EXTRA_NIVEL    = "extra_nivel"
        const val EXTRA_NIVEL_ID = "extra_nivel_id"
    }
}