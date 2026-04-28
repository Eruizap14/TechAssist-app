package com.eloyruiz.techassist.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.eloyruiz.techassist.R
import com.eloyruiz.techassist.data.DatabaseHelper
import com.eloyruiz.techassist.data.TechAssistContract.Usuario
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    // ── Vistas ────────────────────────────────────────────────────────────────
    private lateinit var tilIdTecnico: TextInputLayout
    private lateinit var etIdTecnico: TextInputEditText
    private lateinit var tilContrasena: TextInputLayout
    private lateinit var etContrasena: TextInputEditText
    private lateinit var checkRecordar: CheckBox
    private lateinit var btnAcceder: MaterialButton

    // ── Datos ─────────────────────────────────────────────────────────────────
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var prefs: SharedPreferences

    // ─────────────────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)
        prefs    = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        bindViews()
        cargarSesionRecordada()
        configurarBoton()
    }

    // ── Binding ───────────────────────────────────────────────────────────────

    private fun bindViews() {
        tilIdTecnico  = findViewById(R.id.tilIdTecnico)
        etIdTecnico   = findViewById(R.id.etIdTecnico)
        tilContrasena = findViewById(R.id.tilContrasena)
        etContrasena  = findViewById(R.id.etContrasena)
        checkRecordar = findViewById(R.id.checkRecordar)
        btnAcceder    = findViewById(R.id.btnAcceder)
    }

    // ── Recordar sesión ───────────────────────────────────────────────────────

    private fun cargarSesionRecordada() {
        val idGuardado = prefs.getString(PREF_ID, null)
        if (!idGuardado.isNullOrEmpty()) {
            etIdTecnico.setText(idGuardado)
            checkRecordar.isChecked = true
        }
    }

    // ── Lógica de acceso ──────────────────────────────────────────────────────

    private fun configurarBoton() {
        btnAcceder.setOnClickListener {
            val idTexto    = etIdTecnico.text?.toString()?.trim() ?: ""
            val contrasena = etContrasena.text?.toString()?.trim() ?: ""

            // Validación básica de campos vacíos
            if (idTexto.isEmpty()) {
                tilIdTecnico.error = "Introduce tu ID de técnico"
                return@setOnClickListener
            } else {
                tilIdTecnico.error = null
            }

            if (contrasena.isEmpty()) {
                tilContrasena.error = "Introduce tu contraseña"
                return@setOnClickListener
            } else {
                tilContrasena.error = null
            }

            val usuarioId = idTexto.toIntOrNull()
            if (usuarioId == null) {
                tilIdTecnico.error = "El ID debe ser un número"
                return@setOnClickListener
            }

            // Buscar el usuario en la BD por ID
            val usuario = dbHelper.getUsuarioById(usuarioId)

            if (usuario == null) {
                Snackbar.make(it, "Usuario no encontrado", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // En el MVP la contraseña no está en BD — se acepta cualquiera que no esté vacía.
            // Cuando añadas autenticación real, aquí comparas el hash.

            // Guardar sesión si el checkbox está marcado
            if (checkRecordar.isChecked) {
                prefs.edit().putString(PREF_ID, idTexto).apply()
            } else {
                prefs.edit().remove(PREF_ID).apply()
            }

            val nombre  = usuario[Usuario.COL_NOMBRE] as String
            val rol     = usuario[Usuario.COL_ROL]    as String
            val nivelId = usuario[Usuario.COL_NIVEL_DIGITAL_ID] as Int

            // Redirigir según rol
            val destino: Class<*> = when (rol) {
                "Supervisor", "Administrador" -> SupervisorActivity::class.java
                else                          -> MinitestActivity::class.java
            }

            val intent = Intent(this, destino).apply {
                putExtra(EXTRA_USUARIO_ID,     usuarioId)
                putExtra(EXTRA_USUARIO_NOMBRE, nombre)
                putExtra(EXTRA_USUARIO_ROL,    rol)
                putExtra(EXTRA_NIVEL_ID,       nivelId)
            }
            startActivity(intent)
        }
    }

    // ── Companion ─────────────────────────────────────────────────────────────

    companion object {
        const val EXTRA_USUARIO_ID     = "extra_usuario_id"
        const val EXTRA_USUARIO_NOMBRE = "extra_usuario_nombre"
        const val EXTRA_USUARIO_ROL    = "extra_usuario_rol"
        const val EXTRA_NIVEL_ID       = "extra_nivel_id"

        private const val PREFS_NAME = "techassist_prefs"
        private const val PREF_ID    = "pref_ultimo_id"
    }
}