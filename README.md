# TechAssist

**Asistente inteligente de recomendación de herramientas para técnicos de mantenimiento industrial**

Proyecto de Final de Grado Superior — Desarrollo de Aplicaciones Multiplataforma  
Autor: **Eloy Ruíz Zapata** · Curso 2025/2026

---

## Descripción

TechAssist es una aplicación Android nativa diseñada para apoyar a técnicos de mantenimiento industrial en su trabajo diario. A través de un motor de reglas local, el sistema recomienda la herramienta más adecuada según la categoría de tarea seleccionada, incluyendo una justificación y una guía de uso paso a paso.

La app incorpora un sistema de evaluación del nivel de competencia digital del técnico (minitest), un panel de supervisión con KPIs del grupo y gestión diferenciada por roles.

---

## Funcionalidades principales

- **Login con autenticación SHA-256** y opción de recordar sesión
- **Tres roles diferenciados**: Técnico, Supervisor y Administrador
- **Motor de reglas experto** con 8 categorías de mantenimiento
- **Minitest de nivel digital** adaptativo (Básico, Intermedio, Avanzado)
- **Recomendación de herramienta** con justificación del agente y guía de 3 pasos secuenciales
- **Historial de consultas** por usuario
- **Panel de supervisión** con KPIs: consultas del día y herramienta más usada
- **Modo sin conexión** con banner informativo
- **Exportación de reporte** (preparado para integración con n8n)

---

## Arquitectura y tecnologías

| Elemento | Detalle |
|---|---|
| Lenguaje | Kotlin |
| Plataforma | Android (minSdk 24, targetSdk 36) |
| Versión de app | 1.0 |
| Patrón | MVC (Activities + DatabaseHelper) |
| Base de datos | SQLite local (SQLiteOpenHelper v6) |
| UI | Material Design 3, XML Views |
| Autenticación | SHA-256 |

---

## Roles y flujo de navegación

```
Login
 ├── Técnico ──────→ Minitest (1ª vez) → MainTecnicoActivity
 ├── Supervisor ───→ SupervisorActivity
 └── Administrador → MainTecnicoActivity (con acceso al Panel)
                          └── [Panel] → SupervisorActivity
                                            └── [Volver] → MainTecnicoActivity
```

---

## Usuarios de prueba

| ID | Nombre | Rol | Contraseña |
|---|---|---|---|
| `0` | Administrador | Administrador | `admin` |
| `1001` | Ana González | Supervisor | `superv123` |
| `1234` | Sergio Sánchez | Técnico | `sergio123` |
| `2001` | Carlos Martínez | Técnico | `carlos123` |
| `2002` | Laura Fernández | Técnico | `laura123` |
| `2003` | Miguel Torres | Técnico | `miguel123` |

---

## Categorías del motor de reglas

| Categoría | Herramienta recomendada |
|---|---|
| Correctivo | Módulo de Incidencias (CMMS) |
| Preventivo | Check-list Digital |
| Limpieza | Pistola de Aire Comprimido |
| Eléctrico | Software de Diagnóstico Eléctrico |
| Lubricación | Pistola Engrasadora |
| Inspección | Cámara Termográfica |
| Mecánico | Llave Dinamométrica |
| Predictivo | Plataforma de Análisis de Vibraciones |

---

## Estructura del proyecto

```
TechAssist/
└── mobile/
    └── app/src/main/
        ├── java/com/eloyruiz/techassist/
        │   ├── data/
        │   │   ├── DatabaseHelper.kt
        │   │   └── TechAssistContract.kt
        │   └── ui/
        │       ├── LoginActivity.kt
        │       ├── MainTecnicoActivity.kt
        │       ├── MinitestActivity.kt
        │       ├── ResultadoMinitestActivity.kt
        │       ├── SupervisorActivity.kt
        │       └── TecnicoAdapter.kt
        └── res/
            ├── layout/
            ├── drawable/
            └── menu/
```

---

## Instalación y ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Eruizap14/TechAssist-app.git
   ```
2. Abre el proyecto en **Android Studio** (Panda 4 o superior)
3. Sincroniza Gradle
4. Ejecuta en emulador o dispositivo físico (Android 7.0+)

> La base de datos se crea automáticamente en el primer arranque con todos los datos de prueba incluidos.

---

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.
