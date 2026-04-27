package com.eloyruiz.techassist.data

object TechAssistContract {

    // ─────────────────────────────────────────────
    // NivelDigital
    // ─────────────────────────────────────────────
    object NivelDigital {
        const val TABLE_NAME       = "NivelDigital"
        const val COL_ID           = "id_nivel"
        const val COL_NOMBRE       = "nombre"
        const val COL_DESCRIPCION  = "descripcion"

        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID          INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE      TEXT NOT NULL,
                $COL_DESCRIPCION TEXT
            )
        """

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    // ─────────────────────────────────────────────
    // Herramienta
    // ─────────────────────────────────────────────
    object Herramienta {
        const val TABLE_NAME       = "Herramienta"
        const val COL_ID           = "id_herramienta"
        const val COL_NOMBRE       = "nombre"
        const val COL_DESCRIPCION  = "descripcion"
        const val COL_CATEGORIA    = "categoria"

        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID          INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE      TEXT NOT NULL,
                $COL_DESCRIPCION TEXT,
                $COL_CATEGORIA   TEXT
            )
        """

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    // ─────────────────────────────────────────────
    // Usuario
    // ─────────────────────────────────────────────
    object Usuario {
        const val TABLE_NAME          = "Usuario"
        const val COL_ID              = "id_usuario"
        const val COL_NOMBRE          = "nombre"
        const val COL_ROL             = "rol"
        const val COL_NIVEL_DIGITAL_ID = "nivel_digital_id"

        // Valores válidos para el campo rol
        val ROLES = listOf("Técnico", "Supervisor", "Administrador")

        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID               INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE           TEXT NOT NULL,
                $COL_ROL              TEXT NOT NULL
                                      CHECK($COL_ROL IN ('Técnico','Supervisor','Administrador')),
                $COL_NIVEL_DIGITAL_ID INTEGER NOT NULL,
                FOREIGN KEY ($COL_NIVEL_DIGITAL_ID)
                    REFERENCES ${NivelDigital.TABLE_NAME}(${NivelDigital.COL_ID})
            )
        """

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    // ─────────────────────────────────────────────
    // Regla
    // ─────────────────────────────────────────────
    object Regla {
        const val TABLE_NAME        = "Regla"
        const val COL_ID            = "id_regla"
        const val COL_CATEGORIA     = "categoria_tarea"
        const val COL_HERRAMIENTA_ID = "herramienta_id"
        const val COL_EXPLICACION   = "explicacion"

        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID             INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CATEGORIA      TEXT NOT NULL,
                $COL_HERRAMIENTA_ID INTEGER NOT NULL,
                $COL_EXPLICACION    TEXT,
                FOREIGN KEY ($COL_HERRAMIENTA_ID)
                    REFERENCES ${Herramienta.TABLE_NAME}(${Herramienta.COL_ID})
            )
        """

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    // ─────────────────────────────────────────────
    // Guia
    // ─────────────────────────────────────────────
    object Guia {
        const val TABLE_NAME        = "Guia"
        const val COL_ID            = "id_guia"
        const val COL_HERRAMIENTA_ID = "herramienta_id"
        const val COL_PASO_1        = "paso_1"
        const val COL_PASO_2        = "paso_2"
        const val COL_PASO_3        = "paso_3"

        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID             INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_HERRAMIENTA_ID INTEGER NOT NULL UNIQUE,
                $COL_PASO_1         TEXT,
                $COL_PASO_2         TEXT,
                $COL_PASO_3         TEXT,
                FOREIGN KEY ($COL_HERRAMIENTA_ID)
                    REFERENCES ${Herramienta.TABLE_NAME}(${Herramienta.COL_ID})
            )
        """

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    // ─────────────────────────────────────────────
    // Consulta
    // ─────────────────────────────────────────────
    object Consulta {
        const val TABLE_NAME        = "Consulta"
        const val COL_ID            = "id_consulta"
        const val COL_USUARIO_ID    = "usuario_id"
        const val COL_HERRAMIENTA_ID = "herramienta_id"
        const val COL_FECHA         = "fecha"
        const val COL_CATEGORIA     = "categoria_tarea"

        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COL_ID             INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USUARIO_ID     INTEGER NOT NULL,
                $COL_HERRAMIENTA_ID INTEGER NOT NULL,
                $COL_FECHA          DATE    NOT NULL DEFAULT(DATE('now')),
                $COL_CATEGORIA      TEXT,
                FOREIGN KEY ($COL_USUARIO_ID)
                    REFERENCES ${Usuario.TABLE_NAME}(${Usuario.COL_ID}),
                FOREIGN KEY ($COL_HERRAMIENTA_ID)
                    REFERENCES ${Herramienta.TABLE_NAME}(${Herramienta.COL_ID})
            )
        """

        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}