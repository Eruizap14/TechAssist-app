package com.eloyruiz.techassist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.eloyruiz.techassist.R
import com.google.android.material.button.MaterialButton

class ResultadoMinitestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_minitest)

        val nivel       = intent.getStringExtra(MinitestActivity.EXTRA_NIVEL)    ?: "Básico"
        val nivelId     = intent.getIntExtra(MinitestActivity.EXTRA_NIVEL_ID, 1)
        val usuarioId   = intent.getIntExtra(LoginActivity.EXTRA_USUARIO_ID, -1)
        val nombre      = intent.getStringExtra(LoginActivity.EXTRA_USUARIO_NOMBRE) ?: ""
        val rol         = intent.getStringExtra(LoginActivity.EXTRA_USUARIO_ROL)    ?: ""

        val tvNivel       = findViewById<TextView>(R.id.tvNivelObtenido)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcionNivel)
        val tvExplicacion = findViewById<TextView>(R.id.tvExplicacion)
        val btnContinuar  = findViewById<MaterialButton>(R.id.btnContinuar)

        // Texto del nivel con "Técnico" delante
        tvNivel.text = "Técnico $nivel"

        // Descripción según nivel
        tvDescripcion.text = when (nivel) {
            "Básico"     -> "Usuario con poca experiencia en herramientas digitales"
            "Intermedio" -> "Maneja aplicaciones estándar y tablets con soltura"
            else         -> "Experto en sistemas digitales y diagnóstico remoto"
        }

        // Explicación de qué cambia en la app
        tvExplicacion.text = when (nivel) {
            "Básico"     -> "Las guías serán más detalladas, con pasos simples y explicaciones visuales para ayudarte en cada momento."
            "Intermedio" -> "Recibirás guías equilibradas, con el detalle justo para completar cada tarea de forma eficiente."
            else         -> "Las recomendaciones serán más técnicas y concisas, adaptadas a tu experiencia avanzada."
        }

        // Continuar → pantalla principal del técnico
        btnContinuar.setOnClickListener {
            startActivity(
                Intent(this, MainTecnicoActivity::class.java).apply {
                    putExtra(LoginActivity.EXTRA_USUARIO_ID,     usuarioId)
                    putExtra(LoginActivity.EXTRA_USUARIO_NOMBRE, nombre)
                    putExtra(LoginActivity.EXTRA_USUARIO_ROL,    rol)
                    putExtra(LoginActivity.EXTRA_NIVEL_ID,       nivelId)
                }
            )
            finish()
        }
    }
}