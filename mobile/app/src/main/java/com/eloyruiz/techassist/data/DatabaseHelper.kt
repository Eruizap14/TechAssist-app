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
        const val DATABASE_VERSION = 6
    }

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

        db.execSQL("INSERT INTO ${NivelDigital.TABLE_NAME} (${NivelDigital.COL_NOMBRE}, ${NivelDigital.COL_DESCRIPCION}) VALUES ('Básico', 'Usuario con poca experiencia en herramientas')")
        db.execSQL("INSERT INTO ${NivelDigital.TABLE_NAME} (${NivelDigital.COL_NOMBRE}, ${NivelDigital.COL_DESCRIPCION}) VALUES ('Intermedio', 'Maneja aplicaciones estándar y tablets')")
        db.execSQL("INSERT INTO ${NivelDigital.TABLE_NAME} (${NivelDigital.COL_NOMBRE}, ${NivelDigital.COL_DESCRIPCION}) VALUES ('Avanzado', 'Experto en sistemas y diagnóstico remoto')")

        // Herramientas — mezcla de físicas y software según categoría
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Módulo de Incidencias (CMMS)', 'Registro y seguimiento de averías en el sistema de gestión de mantenimiento', 'Correctivo')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Check-list Digital', 'Lista de verificación periódica del estado de equipos registrada en la app', 'Preventivo')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Pistola de Aire Comprimido', 'Limpieza de componentes electrónicos y mecánicos sin contacto', 'Limpieza')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Software de Diagnóstico Eléctrico', 'Aplicación de análisis y registro de parámetros eléctricos en tiempo real', 'Eléctrico')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Pistola Engrasadora', 'Aplicación de lubricante en rodamientos y guías lineales con cantidad controlada', 'Lubricación')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Cámara Termográfica', 'Detección de puntos calientes y fallos ocultos mediante infrarrojos', 'Inspección')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Llave Dinamométrica', 'Apriete de tornillos con par de torsión controlado según especificación', 'Mecánico')")
        db.execSQL("INSERT INTO ${Herramienta.TABLE_NAME} (${Herramienta.COL_NOMBRE}, ${Herramienta.COL_DESCRIPCION}, ${Herramienta.COL_CATEGORIA}) VALUES ('Plataforma de Análisis de Vibraciones', 'Software de monitorización predictiva de rodamientos y motores por espectro', 'Predictivo')")

        // Reglas del agente experto
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Correctivo', 1, 'Ante una avería inesperada, registrar la incidencia en el CMMS permite asignar prioridad, notificar al supervisor y dejar trazabilidad del diagnóstico y la resolución.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Preventivo', 2, 'El check-list garantiza que ningún punto de revisión quede sin verificar, generando trazabilidad automática de cada intervención con fecha, hora y técnico responsable.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Limpieza', 3, 'La pistola de aire comprimido elimina polvo y partículas de componentes delicados sin contacto físico, reduciendo el riesgo de daño durante la intervención.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Eléctrico', 4, 'El software de diagnóstico eléctrico registra y analiza parámetros como voltaje, corriente y factor de potencia, facilitando la detección de sobrecargas y desequilibrios de fase.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Lubricación', 5, 'La pistola engrasadora aplica la cantidad exacta de lubricante en rodamientos y guías, evitando tanto la falta como el exceso de grasa que podrían dañar el equipo.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Inspección', 6, 'La cámara termográfica detecta anomalías térmicas antes de que produzcan un fallo, permitiendo identificar conexiones flojas o motores sobrecalentados sin detener la producción.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Mecánico', 7, 'La llave dinamométrica garantiza que cada tornillo quede apretado con el par exacto del fabricante, evitando roturas por exceso o aflojamientos por defecto.')")
        db.execSQL("INSERT INTO ${Regla.TABLE_NAME} (${Regla.COL_CATEGORIA}, ${Regla.COL_HERRAMIENTA_ID}, ${Regla.COL_EXPLICACION}) VALUES ('Predictivo', 8, 'La plataforma de análisis de vibraciones detecta patrones anómalos en rodamientos y motores antes del fallo, permitiendo planificar la intervención sin parada de emergencia.')")

        // Guías de uso
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (1, 'Abre el módulo de incidencias del CMMS y crea una nueva orden de trabajo indicando el equipo afectado y la descripción del fallo.', 'Asigna la prioridad (crítica, alta, media o baja) según el impacto en producción y adjunta fotografías del problema si es posible.', 'Guarda la orden y notifica al supervisor. El sistema generará automáticamente un número de referencia para el seguimiento.')")
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (2, 'Abre el check-list correspondiente al equipo y al tipo de revisión (diaria, semanal o mensual) desde la app.', 'Recorre cada punto de verificación marcando el estado: correcto, con observación o requiere intervención.', 'Firma digitalmente el check-list al terminar para que quede registrado con fecha, hora y técnico responsable.')")
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (3, 'Desconecta o aísla el equipo a limpiar y coloca gafas de protección antes de usar la pistola.', 'Dirige el chorro de aire a 10-15 cm del componente, soplando de dentro hacia fuera para no empujar la suciedad al interior.', 'Recoge el polvo expulsado con un paño o aspirador industrial y verifica que no queden residuos en zonas críticas.')")
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (4, 'Abre el software de diagnóstico eléctrico y selecciona el equipo o circuito a analizar desde el listado de activos.', 'Inicia la captura de parámetros en tiempo real. Revisa los valores de voltaje, corriente y factor de potencia frente a los valores nominales.', 'Si detectas una anomalía, guarda el informe generado automáticamente y regístralo como incidencia en el sistema.')")
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (5, 'Consulta la ficha técnica del equipo para conocer el tipo de grasa recomendado y la cantidad en gramos por punto de engrase.', 'Conecta la boquilla de la pistola al niple de engrase y aplica la cantidad indicada con presión constante y controlada.', 'Limpia el exceso de grasa que salga por los retenes y registra la intervención indicando fecha, tipo de grasa y cantidad aplicada.')")
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (6, 'Enciende la cámara y déjala estabilizar 5 minutos. Configura la emisividad según el material (0.95 para superficies oxidadas o pintadas).', 'Escanea el equipo en carga normal a distancia constante. Busca zonas con temperatura significativamente superior a las adyacentes.', 'Guarda las imágenes con anotaciones de temperatura máxima, localización y fecha para incluirlas en el informe de inspección.')")
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (7, 'Consulta la documentación del fabricante para conocer el par de apriete en N·m para cada tornillo o conexión.', 'Ajusta la escala de la llave al valor indicado y selecciona el vaso adecuado al tipo y tamaño del tornillo.', 'Aprieta con movimiento suave y continuo hasta que la llave emita el clic o ceda, indicando que se alcanzó el par programado.')")
        db.execSQL("INSERT INTO ${Guia.TABLE_NAME} (${Guia.COL_HERRAMIENTA_ID}, ${Guia.COL_PASO_1}, ${Guia.COL_PASO_2}, ${Guia.COL_PASO_3}) VALUES (8, 'Accede a la plataforma de análisis de vibraciones y selecciona el activo a monitorizar. Conecta el sensor al punto de medición indicado en el plano del equipo.', 'Inicia la captura con el equipo en condiciones normales de carga durante al menos 30 segundos para obtener un espectro representativo.', 'Compara el espectro con la línea base del equipo. Un aumento de amplitud en frecuencias características indica desgaste o desequilibrio.')")

        val hash1234     = hashSha256("1234")
        val hashAdmin    = hashSha256("admin")
        val hashSergio   = hashSha256("sergio123")
        val hashCarlos   = hashSha256("carlos123")
        val hashLaura    = hashSha256("laura123")
        val hashMiguel   = hashSha256("miguel123")
        val hashSuperv   = hashSha256("superv123")

        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (0,    'Administrador',      'Administrador', 3, '$hashAdmin')")
        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (1001, 'Ana González',       'Supervisor',    3, '$hashSuperv')")
        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (1234, 'Sergio Sánchez',     'Técnico',       1, '$hashSergio')")
        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (2001, 'Carlos Martínez',    'Técnico',       2, '$hashCarlos')")
        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (2002, 'Laura Fernández',    'Técnico',       1, '$hashLaura')")
        db.execSQL("INSERT INTO ${Usuario.TABLE_NAME} (${Usuario.COL_ID}, ${Usuario.COL_NOMBRE}, ${Usuario.COL_ROL}, ${Usuario.COL_NIVEL_DIGITAL_ID}, ${Usuario.COL_CONTRASENA}) VALUES (2003, 'Miguel Torres',      'Técnico',       3, '$hashMiguel')")
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

    fun hashSha256(input: String): String {
        val bytes = java.security.MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

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