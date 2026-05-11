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
        const val DATABASE_VERSION = 3  // ← subido a 3 por herramientas, reglas y guías completas
    }

    // ─────────────────────────────────────────────
    // Ciclo de vida
    // ─────────────────────────────────────────────

    override fun onCreate(db: SQLiteDatabase) {
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

        // ── Herramientas (id 1-8, una por categoría) ───────────────────────
        // id 1 — Correctivo
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Multímetro Digital', 'Medición de voltajes, corrientes y resistencias en circuitos', 'Correctivo')")
        // id 2 — Preventivo
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Check-list Digital', 'Lista de verificación periódica de estado de equipos', 'Preventivo')")
        // id 3 — Limpieza
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Pistola de Aire Comprimido', 'Limpieza de componentes electrónicos y mecánicos', 'Limpieza')")
        // id 4 — Eléctrico
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Pinza Amperimétrica', 'Medición de corriente sin cortar el circuito', 'Eléctrico')")
        // id 5 — Lubricación
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Pistola Engrasadora', 'Aplicación de grasa en rodamientos y guías lineales', 'Lubricación')")
        // id 6 — Inspección
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Cámara Termográfica', 'Detección de puntos calientes y fallos ocultos por infrarrojos', 'Inspección')")
        // id 7 — Mecánico
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Llave Dinamométrica', 'Apriete de tornillos con par de torsión controlado', 'Mecánico')")
        // id 8 — Predictivo
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Analizador de Vibraciones', 'Diagnóstico predictivo de rodamientos y motores por vibración', 'Predictivo')")

        // ── Reglas (agente experto) ────────────────────────────────────────
        // Cada regla asocia una categoría_tarea a una herramienta_id con su explicación
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Correctivo', 1, 'Ante un fallo eléctrico o avería inesperada, el multímetro permite localizar el punto de fallo midiendo voltaje, continuidad y resistencia de forma rápida y segura.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Preventivo', 2, 'El check-list digital garantiza que ningún punto de revisión periódica quede sin verificar, dejando trazabilidad automática de cada intervención.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Limpieza', 3, 'La pistola de aire comprimido elimina polvo y partículas de componentes delicados sin contacto físico, reduciendo el riesgo de daño durante la limpieza.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Eléctrico', 4, 'La pinza amperimétrica mide corriente en tiempo real sin interrumpir el circuito, siendo esencial para diagnosticar sobrecargas y desequilibrios de fase.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Lubricación', 5, 'La pistola engrasadora permite aplicar la cantidad exacta de lubricante en rodamientos y guías, evitando tanto la falta como el exceso de grasa.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Inspección', 6, 'La cámara termográfica detecta anomalías térmicas invisibles al ojo humano, permitiendo identificar conexiones flojas, motores sobrecalentados o fugas antes de que fallen.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Mecánico', 7, 'La llave dinamométrica asegura que cada tornillo quede apretado con el par exacto especificado por el fabricante, evitando roturas por exceso o aflojamientos por defecto.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Predictivo', 8, 'El analizador de vibraciones detecta patrones anómalos en rodamientos y motores antes de que se produzca el fallo, permitiendo planificar la intervención sin parada de emergencia.')")

        // ── Guías de primeros pasos (3 pasos por herramienta) ─────────────
        // Guía herramienta 1 — Multímetro Digital
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (1, 'Selecciona la magnitud a medir (voltaje DC/AC, resistencia o continuidad) girando el selector del multímetro.', 'Conecta las puntas de prueba: la roja al terminal positivo (+) y la negra al COM (-) del multímetro.', 'Aplica las puntas al circuito con la instalación desenergizada para resistencia, o energizada para voltaje, y lee el valor en pantalla.')")
        // Guía herramienta 2 — Check-list Digital
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (2, 'Abre el check-list correspondiente al equipo y al tipo de revisión (diaria, semanal o mensual) desde la app.', 'Recorre cada punto de verificación marcando el estado: correcto, con observación o requiere intervención.', 'Firma digitalmente el check-list al terminar para que quede registrado con fecha, hora y técnico responsable.')")
        // Guía herramienta 3 — Pistola de Aire Comprimido
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (3, 'Desconecta o aísla el equipo a limpiar y coloca gafas de protección antes de usar la pistola.', 'Dirige el chorro de aire a una distancia de 10-15 cm del componente, soplando de dentro hacia fuera para no empujar la suciedad al interior.', 'Recoge el polvo expulsado con un paño o aspirador industrial y verifica que no queden residuos en zonas críticas.')")
        // Guía herramienta 4 — Pinza Amperimétrica
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (4, 'Selecciona el rango de corriente adecuado en la pinza (AC o DC) según el circuito que vayas a medir.', 'Abre la mordaza y rodea con ella un único conductor activo: si incluyes dos conductores la medición se anulará.', 'Lee el valor de corriente en pantalla con el equipo en funcionamiento y compáralo con los valores nominales del fabricante.')")
        // Guía herramienta 5 — Pistola Engrasadora
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (5, 'Consulta la ficha técnica del equipo para conocer el tipo de grasa recomendado y la cantidad en gramos por punto de engrase.', 'Conecta la boquilla de la pistola al niple de engrase y aplica la cantidad indicada con presión constante y controlada.', 'Limpia el exceso de grasa que salga por los retenes y registra la intervención indicando fecha, tipo de grasa y cantidad aplicada.')")
        // Guía herramienta 6 — Cámara Termográfica
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (6, 'Enciende la cámara y deja que se estabilice 5 minutos antes de medir. Configura la emisividad según el material a inspeccionar (0.95 para superficies oxidadas o pintadas).', 'Escanea el equipo con el circuito en carga normal manteniendo una distancia constante. Busca zonas con temperatura significativamente superior a las adyacentes.', 'Guarda las imágenes termográficas con anotaciones de temperatura máxima, localización y fecha para incluirlas en el informe de inspección.')")
        // Guía herramienta 7 — Llave Dinamométrica
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (7, 'Consulta la documentación del fabricante para conocer el par de apriete especificado en N·m para cada tornillo o conexión.', 'Ajusta la escala de la llave dinamométrica al valor indicado y selecciona el vaso adecuado al tipo y tamaño del tornillo.', 'Aprieta el tornillo con movimiento suave y continuo hasta que la llave emita el clic o ceda, indicando que se alcanzó el par programado.')")
        // Guía herramienta 8 — Analizador de Vibraciones
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (8, 'Fija el acelerómetro en la carcasa del rodamiento o motor en la dirección de medición (horizontal, vertical y axial) con imán o adhesivo.', 'Inicia la captura con el equipo funcionando en condiciones normales de carga. Registra el espectro de frecuencias durante al menos 30 segundos.', 'Compara el espectro obtenido con la línea base del equipo. Un aumento de amplitud en frecuencias características indica desgaste de rodamiento o desequilibrio.')")

        // ── Usuarios ───────────────────────────────────────────────────────
        val hash1234  = hashSha256("1234")
        val hashAdmin = hashSha256("admin")

        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (1234, 'Técnico Demo', 'Técnico', 1, '$hash1234')")
        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (0, 'Administrador', 'Administrador', 3, '$hashAdmin')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
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
        if (!db.isReadOnly) {
            db.execSQL("PRAGMA foreign_keys = ON;")
        }
    }

    // ─────────────────────────────────────────────
    // Hash SHA-256
    // ─────────────────────────────────────────────

    fun hashSha256(input: String): String {
        val bytes = java.security.MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
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
                result.add(mapOf(
                    NivelDigital.COL_ID          to it.getInt(it.getColumnIndexOrThrow(NivelDigital.COL_ID)),
                    NivelDigital.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(NivelDigital.COL_NOMBRE)),
                    NivelDigital.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(NivelDigital.COL_DESCRIPCION))
                ))
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
        return writableDatabase.update(NivelDigital.TABLE_NAME, values,
            "${NivelDigital.COL_ID} = ?", arrayOf(id.toString()))
    }

    fun deleteNivelDigital(id: Int): Int =
        writableDatabase.delete(NivelDigital.TABLE_NAME,
            "${NivelDigital.COL_ID} = ?", arrayOf(id.toString()))

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
                result.add(mapOf(
                    Herramienta.COL_ID          to it.getInt(it.getColumnIndexOrThrow(Herramienta.COL_ID)),
                    Herramienta.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_NOMBRE)),
                    Herramienta.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_DESCRIPCION)),
                    Herramienta.COL_CATEGORIA   to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_CATEGORIA))
                ))
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
                result.add(mapOf(
                    Herramienta.COL_ID          to it.getInt(it.getColumnIndexOrThrow(Herramienta.COL_ID)),
                    Herramienta.COL_NOMBRE      to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_NOMBRE)),
                    Herramienta.COL_DESCRIPCION to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_DESCRIPCION)),
                    Herramienta.COL_CATEGORIA   to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_CATEGORIA))
                ))
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
        return writableDatabase.update(Herramienta.TABLE_NAME, values,
            "${Herramienta.COL_ID} = ?", arrayOf(id.toString()))
    }

    fun deleteHerramienta(id: Int): Int =
        writableDatabase.delete(Herramienta.TABLE_NAME,
            "${Herramienta.COL_ID} = ?", arrayOf(id.toString()))

    // ─────────────────────────────────────────────
    // Usuario – CRUD
    // ─────────────────────────────────────────────

    fun insertUsuario(nombre: String, rol: String, nivelDigitalId: Int, contrasena: String): Long {
        require(rol in Usuario.ROLES) { "Rol no válido: $rol" }
        val values = ContentValues().apply {
            put(Usuario.COL_NOMBRE, nombre)
            put(Usuario.COL_ROL, rol)
            put(Usuario.COL_NIVEL_DIGITAL_ID, nivelDigitalId)
            put(Usuario.COL_CONTRASENA, hashSha256(contrasena))
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
                result.add(mapOf(
                    Usuario.COL_ID               to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_ID)),
                    Usuario.COL_NOMBRE           to it.getString(it.getColumnIndexOrThrow(Usuario.COL_NOMBRE)),
                    Usuario.COL_ROL              to it.getString(it.getColumnIndexOrThrow(Usuario.COL_ROL)),
                    Usuario.COL_NIVEL_DIGITAL_ID to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_NIVEL_DIGITAL_ID)),
                    Usuario.COL_CONTRASENA       to it.getString(it.getColumnIndexOrThrow(Usuario.COL_CONTRASENA))
                ))
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
                Usuario.COL_NIVEL_DIGITAL_ID to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_NIVEL_DIGITAL_ID)),
                Usuario.COL_CONTRASENA       to it.getString(it.getColumnIndexOrThrow(Usuario.COL_CONTRASENA))
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
                result.add(mapOf(
                    Usuario.COL_ID               to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_ID)),
                    Usuario.COL_NOMBRE           to it.getString(it.getColumnIndexOrThrow(Usuario.COL_NOMBRE)),
                    Usuario.COL_ROL              to it.getString(it.getColumnIndexOrThrow(Usuario.COL_ROL)),
                    Usuario.COL_NIVEL_DIGITAL_ID to it.getInt(it.getColumnIndexOrThrow(Usuario.COL_NIVEL_DIGITAL_ID)),
                    Usuario.COL_CONTRASENA       to it.getString(it.getColumnIndexOrThrow(Usuario.COL_CONTRASENA))
                ))
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
        return writableDatabase.update(Usuario.TABLE_NAME, values,
            "${Usuario.COL_ID} = ?", arrayOf(id.toString()))
    }

    fun deleteUsuario(id: Int): Int =
        writableDatabase.delete(Usuario.TABLE_NAME,
            "${Usuario.COL_ID} = ?", arrayOf(id.toString()))

    // ─────────────────────────────────────────────
    // Regla – CRUD
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
                result.add(mapOf(
                    Regla.COL_ID             to it.getInt(it.getColumnIndexOrThrow(Regla.COL_ID)),
                    Regla.COL_CATEGORIA      to it.getString(it.getColumnIndexOrThrow(Regla.COL_CATEGORIA)),
                    Regla.COL_HERRAMIENTA_ID to it.getInt(it.getColumnIndexOrThrow(Regla.COL_HERRAMIENTA_ID)),
                    Regla.COL_EXPLICACION    to it.getString(it.getColumnIndexOrThrow(Regla.COL_EXPLICACION))
                ))
            }
        }
        return result
    }

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
                Regla.COL_ID              to it.getInt(it.getColumnIndexOrThrow(Regla.COL_ID)),
                Regla.COL_CATEGORIA       to it.getString(it.getColumnIndexOrThrow(Regla.COL_CATEGORIA)),
                Regla.COL_EXPLICACION     to it.getString(it.getColumnIndexOrThrow(Regla.COL_EXPLICACION)),
                "herramienta_id"          to it.getInt(it.getColumnIndexOrThrow("herramienta_id")),
                "herramienta_nombre"      to it.getString(it.getColumnIndexOrThrow("herramienta_nombre")),
                "herramienta_descripcion" to it.getString(it.getColumnIndexOrThrow("herramienta_descripcion")),
                "herramienta_categoria"   to it.getString(it.getColumnIndexOrThrow("herramienta_categoria"))
            ) else null
        }
    }

    fun updateRegla(id: Int, categoriaTarea: String, herramientaId: Int, explicacion: String?): Int {
        val values = ContentValues().apply {
            put(Regla.COL_CATEGORIA, categoriaTarea)
            put(Regla.COL_HERRAMIENTA_ID, herramientaId)
            put(Regla.COL_EXPLICACION, explicacion)
        }
        return writableDatabase.update(Regla.TABLE_NAME, values,
            "${Regla.COL_ID} = ?", arrayOf(id.toString()))
    }

    fun deleteRegla(id: Int): Int =
        writableDatabase.delete(Regla.TABLE_NAME,
            "${Regla.COL_ID} = ?", arrayOf(id.toString()))

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
        return writableDatabase.update(Guia.TABLE_NAME, values,
            "${Guia.COL_HERRAMIENTA_ID} = ?", arrayOf(herramientaId.toString()))
    }

    fun deleteGuia(herramientaId: Int): Int =
        writableDatabase.delete(Guia.TABLE_NAME,
            "${Guia.COL_HERRAMIENTA_ID} = ?", arrayOf(herramientaId.toString()))

    // ─────────────────────────────────────────────
    // Consulta – CRUD
    // ─────────────────────────────────────────────

    fun insertConsulta(usuarioId: Int, herramientaId: Int, categoriaTarea: String?): Long {
        val values = ContentValues().apply {
            put(Consulta.COL_USUARIO_ID, usuarioId)
            put(Consulta.COL_HERRAMIENTA_ID, herramientaId)
            put(Consulta.COL_CATEGORIA, categoriaTarea)
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
                result.add(mapOf(
                    Consulta.COL_ID             to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_ID)),
                    Consulta.COL_USUARIO_ID     to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_USUARIO_ID)),
                    Consulta.COL_HERRAMIENTA_ID to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_HERRAMIENTA_ID)),
                    Consulta.COL_FECHA          to it.getString(it.getColumnIndexOrThrow(Consulta.COL_FECHA)),
                    Consulta.COL_CATEGORIA      to it.getString(it.getColumnIndexOrThrow(Consulta.COL_CATEGORIA))
                ))
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
                result.add(mapOf(
                    Consulta.COL_ID             to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_ID)),
                    Consulta.COL_USUARIO_ID     to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_USUARIO_ID)),
                    Consulta.COL_HERRAMIENTA_ID to it.getInt(it.getColumnIndexOrThrow(Consulta.COL_HERRAMIENTA_ID)),
                    Consulta.COL_FECHA          to it.getString(it.getColumnIndexOrThrow(Consulta.COL_FECHA)),
                    Consulta.COL_CATEGORIA      to it.getString(it.getColumnIndexOrThrow(Consulta.COL_CATEGORIA))
                ))
            }
        }
        return result
    }

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
                result.add(mapOf(
                    Herramienta.COL_ID     to it.getInt(it.getColumnIndexOrThrow(Herramienta.COL_ID)),
                    Herramienta.COL_NOMBRE to it.getString(it.getColumnIndexOrThrow(Herramienta.COL_NOMBRE)),
                    "total_consultas"      to it.getInt(it.getColumnIndexOrThrow("total_consultas"))
                ))
            }
        }
        return result
    }

    fun deleteConsulta(id: Int): Int =
        writableDatabase.delete(Consulta.TABLE_NAME,
            "${Consulta.COL_ID} = ?", arrayOf(id.toString()))
}