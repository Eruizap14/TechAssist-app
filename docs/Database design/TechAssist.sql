CREATE TABLE IF NOT EXISTS NivelDigital(
    id_nivel INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS Herramienta(
    id_herramienta INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    categoria TEXT
);

CREATE TABLE IF NOT EXISTS Usuario (
    id_usuario   INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre       TEXT NOT NULL,
    rol          TEXT NOT NULL CHECK(rol IN ('Técnico', 'Supervisor', 'Administrador')),
    nivel_digital_id INTEGER NOT NULL,
    contrasena   TEXT NOT NULL,
    FOREIGN KEY (nivel_digital_id) REFERENCES NivelDigital(id_nivel)
);

CREATE TABLE IF NOT EXISTS Regla(
    id_regla INTEGER PRIMARY KEY AUTOINCREMENT,
    categoria_tarea TEXT NOT NULL,
    herramienta_id INTEGER NOT NULL,
    explicacion TEXT,
    FOREIGN KEY (herramienta_id) REFERENCES Herramienta(id_herramienta)
);

CREATE TABLE IF NOT EXISTS Guia(
    id_guia INTEGER PRIMARY KEY AUTOINCREMENT,
    herramienta_id INTEGER NOT NULL UNIQUE,
    paso_1 TEXT,
    paso_2 TEXT,
    paso_3 TEXT,
    FOREIGN KEY (herramienta_id) REFERENCES Herramienta(id_herramienta)
);

CREATE TABLE IF NOT EXISTS Consulta(
    id_consulta INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    herramienta_id INTEGER NOT NULL,
    fecha DATE NOT NULL DEFAULT(DATE('now')),
    categoria_tarea TEXT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (herramienta_id) REFERENCES Herramienta(id_herramienta)
);