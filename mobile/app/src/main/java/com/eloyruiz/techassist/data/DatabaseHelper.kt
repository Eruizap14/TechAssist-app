package com.eloyruiz.techassist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.eloyruiz.techassist.data.TechAssistContract.NivelDigital
import com.eloyruiz.techassist.data.TechAssistContract.Herramienta
import com.eloyruiz.techassist.data.TechAssistContract.Usuario
import com.eloyruiz.techassist.data.TechAssistContract.Regla
import com.eloyruiz.techassist.data.TechAssistContract.Guia
import com.eloyruiz.techassist.data.TechAssistContract.Consulta

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME    = "techassist.db"
        const val DATABASE_VERSION = 1
    }

    // ─────────────────────────────────────────────
    // Ciclo de vida
    // ─────────────────────────────────────────────

    override fun onCreate(db: SQLiteDatabase) {
        // El orden respeta las dependencias de clave foránea
        db.execSQL(NivelDigital.CREATE_TABLE)
        db.execSQL(Herramienta.CREATE_TABLE)
        db.execSQL(Usuario.CREATE_TABLE)
        db.execSQL(Regla.CREATE_TABLE)
        db.execSQL(Guia.CREATE_TABLE)
        db.execSQL(Consulta.CREATE_TABLE)

        seedData(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // ── Niveles Digitales ──────────────────────────────────────────────
        db.execSQL("INSERT INTO ${NivelDigital.TABLE_NAME} (${NivelDigital.COL_NOMBRE}, ${NivelDigital.COL_DESCRIPCION}) VALUES ('Básico', 'Usuario con poca experiencia en herramientas digitales')")
        db.execSQL("INSERT INTO ${NivelDigital.TABLE_NAME} (${NivelDigital.COL_NOMBRE}, ${NivelDigital.COL_DESCRIPCION}) VALUES ('Intermedio', 'Maneja aplicaciones estándar y tablets')")
        db.execSQL("INSERT INTO ${NivelDigital.TABLE_NAME} (${NivelDigital.COL_NOMBRE}, ${NivelDigital.COL_DESCRIPCION}) VALUES ('Avanzado', 'Experto en sistemas digitales y diagnóstico remoto')")

        // ── Herramientas ───────────────────────────────────────────────────
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Multímetro Digital', 'Medición de voltajes y resistencias', 'Electricidad')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Analizador de Vibraciones', 'Diagnóstico de rodamientos y motores', 'Mecánica')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Orden inverso para respetar las FK al eliminar
        db.execSQL(Consulta.DROP_TABLE)
        db.execSQL(Guia.DROP_TABLE)
        db.execSQL(Regla.DROP_TABLE)
        db.execSQL(Usuario.DROP_TABLE)
        db.execSQL(Herramienta.DROP_TABLE)
        db.execSQL(NivelDigital.DROP_TABLE)
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        // Activa el soporte de claves foráneas en cada conexión
        if (!db.isReadOnly) {
            db.execSQL("PRAGMA foreign_keys = ON;")
        }
    }

    // ─────────────────────────────────────────────
    // NivelDigital – CRUD
    // ─────────────────────────────────────────────

    fun insertNivelDigital(nombre: String, descripcion: String?): Long {
        val values = ContentValues().apply {
            put(NivelDigital.COL_NOMBRE, nombre)
            put(NivelDigital.COL_DESCRIPCION, descripcion)
        }
        return writableDatabase.insert(NivelDigital.TABLE_NAME, null, values)
    }

    fun getAllNivelesDigitales(): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            NivelDigital.TABLE_NAME, null, null, null, null, null,
            "${NivelDigital.COL_ID} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        NivelDigital.COL_ID          to it.getInt(it.getColumnIndexOrThrow(NivelDigital.COL_ID)),
                        NivelDigital.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(NivelDigital.COL_NOMBRE)),
                        NivelDigital.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(NivelDigital.COL_DESCRIPCION))
                    )
                )
            }
        }
        return result
    }

    fun getNivelDigitalById(id: Int): Map<String, Any?>? {
        val cursor = readableDatabase.query(
            NivelDigital.TABLE_NAME, null,
            "${NivelDigital.COL_ID} = ?", arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) mapOf(
                NivelDigital.COL_ID          to it.getInt(it.getColumnIndexOrThrow(NivelDigital.COL_ID)),
                NivelDigital.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(NivelDigital.COL_NOMBRE)),
                NivelDigital.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(NivelDigital.COL_DESCRIPCION))
            ) else null
        }
    }

    fun updateNivelDigital(id: Int, nombre: String, descripcion: String?): Int {
        val values = ContentValues().apply {
            put(NivelDigital.COL_NOMBRE, nombre)
            put(NivelDigital.COL_DESCRIPCION, descripcion)
        }
        return writableDatabase.update(
            NivelDigital.TABLE_NAME, values,
            "${NivelDigital.COL_ID} = ?", arrayOf(id.toString())
        )
    }

    fun deleteNivelDigital(id: Int): Int =
        writableDatabase.delete(
            NivelDigital.TABLE_NAME,
            "${NivelDigital.COL_ID} = ?", arrayOf(id.toString())
        )

    // ─────────────────────────────────────────────
    // Herramienta – CRUD
    // ─────────────────────────────────────────────

    fun insertHerramienta(nombre: String, descripcion: String?, categoria: String?): Long {
        val values = ContentValues().apply {
            put(Herramienta.COL_NOMBRE, nombre)
            put(Herramienta.COL_DESCRIPCION, descripcion)
            put(Herramienta.COL_CATEGORIA, categoria)
        }
        return writableDatabase.insert(Herramienta.TABLE_NAME, null, values)
    }

    fun getAllHerramientas(): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            Herramienta.TABLE_NAME, null, null, null, null, null,
            "${Herramienta.COL_ID} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Herramienta.COL_ID          to it.getInt(it.getColumnIndexOrThrow(Herramienta.COL_ID)),
                        Herramienta.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_NOMBRE)),
                        Herramienta.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_DESCRIPCION)),
                        Herramienta.COL_CATEGORIA   to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_CATEGORIA))
                    )
                )
            }
        }
        return result
    }

    fun getHerramientaById(id: Int): Map<String, Any?>? {
        val cursor = readableDatabase.query(
            Herramienta.TABLE_NAME, null,
            "${Herramienta.COL_ID} = ?", arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) mapOf(
                Herramienta.COL_ID          to it.getInt(it.getColumnIndexOrThrow(Herramienta.COL_ID)),
                Herramienta.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_NOMBRE)),
                Herramienta.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_DESCRIPCION)),
                Herramienta.COL_CATEGORIA   to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_CATEGORIA))
            ) else null
        }
    }

    fun getHerramientasByCategoria(categoria: String): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            Herramienta.TABLE_NAME, null,
            "${Herramienta.COL_CATEGORIA} = ?", arrayOf(categoria),
            null, null, "${Herramienta.COL_NOMBRE} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Herramienta.COL_ID          to it.getInt(it.getColumnIndexOrThrow(Herramienta.COL_ID)),
                        Herramienta.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_NOMBRE)),
                        Herramienta.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_DESCRIPCION)),
                        Herramienta.COL_CATEGORIA   to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_CATEGORIA))
                    )
                )
            }
        }
        return result
    }

    fun updateHerramienta(id: Int, nombre: String, descripcion: String?, categoria: String?): Int {
        val values = ContentValues().apply {
            put(Herramienta.COL_NOMBRE, nombre)
            put(Herramienta.COL_DESCRIPCION, descripcion)
            put(Herramienta.COL_CATEGORIA, categoria)
        }
        return writableDatabase.update(
            Herramienta.TABLE_NAME, values,
            "${Herramienta.COL_ID} = ?", arrayOf(id.toString())
        )
    }

    fun deleteHerramienta(id: Int): Int =
        writableDatabase.delete(
            Herramienta.TABLE_NAME,
            "${Herramienta.COL_ID} = ?", arrayOf(id.toString())
        )

    // ─────────────────────────────────────────────
    // Usuario – CRUD
    // ─────────────────────────────────────────────

    fun insertUsuario(nombre: String, rol: String, nivelDigitalId: Int): Long {
        require(rol in Usuario.ROLES) { "Rol no válido: $rol" }
        val values = ContentValues().apply {
            put(Usuario.COL_NOMBRE, nombre)
            put(Usuario.COL_ROL, rol)
            put(Usuario.COL_NIVEL_DIGITAL_ID, nivelDigitalId)
        }
        return writableDatabase.insert(Usuario.TABLE_NAME, null, values)
    }

    fun getAllUsuarios(): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            Usuario.TABLE_NAME, null, null, null, null, null,
            "${Usuario.COL_ID} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Usuario.COL_ID               to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_ID)),
                        Usuario.COL_NOMBRE           to it.getString(it.getColumnIndexOrThrow(Usuario.COL_NOMBRE)),
                        Usuario.COL_ROL              to it.getString(it.getColumnIndexOrThrow(Usuario.COL_ROL)),
                        Usuario.COL_NIVEL_DIGITAL_ID to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_NIVEL_DIGITAL_ID))
                    )
                )
            }
        }
        return result
    }

    fun getUsuarioById(id: Int): Map<String, Any?>? {
        val cursor = readableDatabase.query(
            Usuario.TABLE_NAME, null,
            "${Usuario.COL_ID} = ?", arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) mapOf(
                Usuario.COL_ID               to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_ID)),
                Usuario.COL_NOMBRE           to it.getString(it.getColumnIndexOrThrow(Usuario.COL_NOMBRE)),
                Usuario.COL_ROL              to it.getString(it.getColumnIndexOrThrow(Usuario.COL_ROL)),
                Usuario.COL_NIVEL_DIGITAL_ID to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_NIVEL_DIGITAL_ID))
            ) else null
        }
    }

    fun getUsuariosByRol(rol: String): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            Usuario.TABLE_NAME, null,
            "${Usuario.COL_ROL} = ?", arrayOf(rol),
            null, null, "${Usuario.COL_NOMBRE} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Usuario.COL_ID               to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_ID)),
                        Usuario.COL_NOMBRE           to it.getString(it.getColumnIndexOrThrow(Usuario.COL_NOMBRE)),
                        Usuario.COL_ROL              to it.getString(it.getColumnIndexOrThrow(Usuario.COL_ROL)),
                        Usuario.COL_NIVEL_DIGITAL_ID to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_NIVEL_DIGITAL_ID))
                    )
                )
            }
        }
        return result
    }

    fun updateUsuario(id: Int, nombre: String, rol: String, nivelDigitalId: Int): Int {
        require(rol in Usuario.ROLES) { "Rol no válido: $rol" }
        val values = ContentValues().apply {
            put(Usuario.COL_NOMBRE, nombre)
            put(Usuario.COL_ROL, rol)
            put(Usuario.COL_NIVEL_DIGITAL_ID, nivelDigitalId)
        }
        return writableDatabase.update(
            Usuario.TABLE_NAME, values,
            "${Usuario.COL_ID} = ?", arrayOf(id.toString())
        )
    }

    fun deleteUsuario(id: Int): Int =
        writableDatabase.delete(
            Usuario.TABLE_NAME,
            "${Usuario.COL_ID} = ?", arrayOf(id.toString())
        )

    // ─────────────────────────────────────────────
    // Regla – CRUD  (núcleo del agente experto)
    // ─────────────────────────────────────────────

    fun insertRegla(categoriaTarea: String, herramientaId: Int, explicacion: String?): Long {
        val values = ContentValues().apply {
            put(Regla.COL_CATEGORIA, categoriaTarea)
            put(Regla.COL_HERRAMIENTA_ID, herramientaId)
            put(Regla.COL_EXPLICACION, explicacion)
        }
        return writableDatabase.insert(Regla.TABLE_NAME, null, values)
    }

    fun getAllReglas(): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            Regla.TABLE_NAME, null, null, null, null, null,
            "${Regla.COL_ID} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Regla.COL_ID             to it.getInt(it.getColumnIndexOrThrow(Regla.COL_ID)),
                        Regla.COL_CATEGORIA      to it.getString(it.getColumnIndexOrThrow(Regla.COL_CATEGORIA)),
                        Regla.COL_HERRAMIENTA_ID to it.getInt(it.getColumnIndexOrThrow(Regla.COL_HERRAMIENTA_ID)),
                        Regla.COL_EXPLICACION    to it.getString(it.getColumnIndexOrThrow(Regla.COL_EXPLICACION))
                    )
                )
            }
        }
        return result
    }

    /**
     * Consulta clave del agente: dada una categoría de tarea devuelve la herramienta
     * recomendada con su explicación (JOIN Regla → Herramienta).
     */
    fun getRecomendacionPorCategoria(categoriaTarea: String): Map<String, Any?>? {
        val query = """
            SELECT r.${Regla.COL_ID},
                   r.${Regla.COL_CATEGORIA},
                   r.${Regla.COL_EXPLICACION},
                   h.${Herramienta.COL_ID}          AS herramienta_id,
                   h.${Herramienta.COL_NOMBRE}      AS herramienta_nombre,
                   h.${Herramienta.COL_DESCRIPCION} AS herramienta_descripcion,
                   h.${Herramienta.COL_CATEGORIA}   AS herramienta_categoria
            FROM ${Regla.TABLE_NAME} r
            INNER JOIN ${Herramienta.TABLE_NAME} h
                ON r.${Regla.COL_HERRAMIENTA_ID} = h.${Herramienta.COL_ID}
            WHERE r.${Regla.COL_CATEGORIA} = ?
            LIMIT 1
        """
        val cursor = readableDatabase.rawQuery(query, arrayOf(categoriaTarea))
        return cursor.use {
            if (it.moveToFirst()) mapOf(
                Regla.COL_ID                  to it.getInt(it.getColumnIndexOrThrow(Regla.COL_ID)),
                Regla.COL_CATEGORIA           to it.getString(it.getColumnIndexOrThrow(Regla.COL_CATEGORIA)),
                Regla.COL_EXPLICACION         to it.getString(it.getColumnIndexOrThrow(Regla.COL_EXPLICACION)),
                "herramienta_id"              to it.getInt(it.getColumnIndexOrThrow("herramienta_id")),
                "herramienta_nombre"          to it.getString(it.getColumnIndexOrThrow("herramienta_nombre")),
                "herramienta_descripcion"     to it.getString(it.getColumnIndexOrThrow("herramienta_descripcion")),
                "herramienta_categoria"       to it.getString(it.getColumnIndexOrThrow("herramienta_categoria"))
            ) else null
        }
    }

    fun updateRegla(id: Int, categoriaTarea: String, herramientaId: Int, explicacion: String?): Int {
        val values = ContentValues().apply {
            put(Regla.COL_CATEGORIA, categoriaTarea)
            put(Regla.COL_HERRAMIENTA_ID, herramientaId)
            put(Regla.COL_EXPLICACION, explicacion)
        }
        return writableDatabase.update(
            Regla.TABLE_NAME, values,
            "${Regla.COL_ID} = ?", arrayOf(id.toString())
        )
    }

    fun deleteRegla(id: Int): Int =
        writableDatabase.delete(
            Regla.TABLE_NAME,
            "${Regla.COL_ID} = ?", arrayOf(id.toString())
        )

    // ─────────────────────────────────────────────
    // Guia – CRUD
    // ─────────────────────────────────────────────

    fun insertGuia(herramientaId: Int, paso1: String?, paso2: String?, paso3: String?): Long {
        val values = ContentValues().apply {
            put(Guia.COL_HERRAMIENTA_ID, herramientaId)
            put(Guia.COL_PASO_1, paso1)
            put(Guia.COL_PASO_2, paso2)
            put(Guia.COL_PASO_3, paso3)
        }
        return writableDatabase.insert(Guia.TABLE_NAME, null, values)
    }

    fun getGuiaByHerramientaId(herramientaId: Int): Map<String, Any?>? {
        val cursor = readableDatabase.query(
            Guia.TABLE_NAME, null,
            "${Guia.COL_HERRAMIENTA_ID} = ?", arrayOf(herramientaId.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) mapOf(
                Guia.COL_ID             to it.getInt(it.getColumnIndexOrThrow(Guia.COL_ID)),
                Guia.COL_HERRAMIENTA_ID to it.getInt(it.getColumnIndexOrThrow(Guia.COL_HERRAMIENTA_ID)),
                Guia.COL_PASO_1         to it.getString(it.getColumnIndexOrThrow(Guia.COL_PASO_1)),
                Guia.COL_PASO_2         to it.getString(it.getColumnIndexOrThrow(Guia.COL_PASO_2)),
                Guia.COL_PASO_3         to it.getString(it.getColumnIndexOrThrow(Guia.COL_PASO_3))
            ) else null
        }
    }

    fun updateGuia(herramientaId: Int, paso1: String?, paso2: String?, paso3: String?): Int {
        val values = ContentValues().apply {
            put(Guia.COL_PASO_1, paso1)
            put(Guia.COL_PASO_2, paso2)
            put(Guia.COL_PASO_3, paso3)
        }
        return writableDatabase.update(
            Guia.TABLE_NAME, values,
            "${Guia.COL_HERRAMIENTA_ID} = ?", arrayOf(herramientaId.toString())
        )
    }

    fun deleteGuia(herramientaId: Int): Int =
        writableDatabase.delete(
            Guia.TABLE_NAME,
            "${Guia.COL_HERRAMIENTA_ID} = ?", arrayOf(herramientaId.toString())
        )

    // ─────────────────────────────────────────────
    // Consulta – CRUD  (telemetría / monitorización)
    // ─────────────────────────────────────────────

    fun insertConsulta(usuarioId: Int, herramientaId: Int, categoriaTarea: String?): Long {
        val values = ContentValues().apply {
            put(Consulta.COL_USUARIO_ID, usuarioId)
            put(Consulta.COL_HERRAMIENTA_ID, herramientaId)
            put(Consulta.COL_CATEGORIA, categoriaTarea)
            // COL_FECHA usa DEFAULT(DATE('now')) de SQLite; se puede omitir
        }
        return writableDatabase.insert(Consulta.TABLE_NAME, null, values)
    }

    fun getAllConsultas(): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            Consulta.TABLE_NAME, null, null, null, null, null,
            "${Consulta.COL_FECHA} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Consulta.COL_ID             to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_ID)),
                        Consulta.COL_USUARIO_ID     to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_USUARIO_ID)),
                        Consulta.COL_HERRAMIENTA_ID to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_HERRAMIENTA_ID)),
                        Consulta.COL_FECHA          to it.getString(it.getColumnIndexOrThrow(Consulta.COL_FECHA)),
                        Consulta.COL_CATEGORIA      to it.getString(it.getColumnIndexOrThrow(Consulta.COL_CATEGORIA))
                    )
                )
            }
        }
        return result
    }

    fun getConsultasByUsuario(usuarioId: Int): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.query(
            Consulta.TABLE_NAME, null,
            "${Consulta.COL_USUARIO_ID} = ?", arrayOf(usuarioId.toString()),
            null, null, "${Consulta.COL_FECHA} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Consulta.COL_ID             to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_ID)),
                        Consulta.COL_USUARIO_ID     to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_USUARIO_ID)),
                        Consulta.COL_HERRAMIENTA_ID to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_HERRAMIENTA_ID)),
                        Consulta.COL_FECHA          to it.getString(it.getColumnIndexOrThrow(Consulta.COL_FECHA)),
                        Consulta.COL_CATEGORIA      to it.getString(it.getColumnIndexOrThrow(Consulta.COL_CATEGORIA))
                    )
                )
            }
        }
        return result
    }

    /**
     * Panel de supervisión: herramientas más consultadas con su frecuencia.
     */
    fun getHerramientasMasConsultadas(limit: Int = 10): List<Map<String, Any?>> {
        val query = """
            SELECT h.${Herramienta.COL_ID},
                   h.${Herramienta.COL_NOMBRE},
                   COUNT(*) AS total_consultas
            FROM ${Consulta.TABLE_NAME} c
            INNER JOIN ${Herramienta.TABLE_NAME} h
                ON c.${Consulta.COL_HERRAMIENTA_ID} = h.${Herramienta.COL_ID}
            GROUP BY h.${Herramienta.COL_ID}
            ORDER BY total_consultas DESC
            LIMIT ?
        """
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = readableDatabase.rawQuery(query, arrayOf(limit.toString()))
        cursor.use {
            while (it.moveToNext()) {
                result.add(
                    mapOf(
                        Herramienta.COL_ID      to it.getInt(it.getColumnIndexOrThrow(Herramienta.COL_ID)),
                        Herramienta.COL_NOMBRE  to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_NOMBRE)),
                        "total_consultas"       to it.getInt(it.getColumnIndexOrThrow("total_consultas"))
                    )
                )
            }
        }
        return result
    }

    fun deleteConsulta(id: Int): Int =
        writableDatabase.delete(
            Consulta.TABLE_NAME,
            "${Consulta.COL_ID} = ?", arrayOf(id.toString())
        )
}