package org.example.config

import org.lighthousegames.logging.logging
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.pathString

object Config {
    private val logger = logging()

    var databaseUrl: String = "jdbc:h2:mem:estudiantes"
        private set // La propiedad es mutable (puede cambiar de valor), pero solo dentro del mismo archivo o clase en el que está definida. Sin embargo, fuera de esta clase/objeto, la propiedad es de solo lectura.
    var databaseInitTables: Boolean = false
        private set
    var databaseInitData: Boolean = false
        private set
    var storageData: String = "data"
        private set
    var storageOutput: String = "backup"
        private set
    var cacheSize = 10L
        private set
    var cacheExpiration = 300L

    init {
        try {
            logger.debug { "Cargando configuración" }

            val properties = Properties()
            properties.load(ClassLoader.getSystemResourceAsStream("config.properties"))

            databaseUrl =
                properties.getProperty("database.url", this.databaseUrl)

            databaseInitTables =
                properties.getProperty("database.init.tables", this.databaseInitTables.toString()).toBoolean()

            databaseInitData =
                properties.getProperty("database.init.data", this.databaseInitData.toString()).toBoolean()

            storageData =
                properties.getProperty("storage.data", this.storageData)

            storageOutput =
                properties.getProperty("storage.output", this.storageOutput)

            val directorioActual = System.getProperty("user.dir")
            val directorioOutput = Path.of(directorioActual, storageOutput).pathString
            crearDirectorios(directorioOutput)

            logger.debug { "Configuración cargada correctamente" }

        } catch (e: Exception) {
            logger.error { "Error cargando configuración: ${e.message}" }
            logger.error { "Usando valores por defecto" }
        }

    }

    private fun crearDirectorios(vararg directorios: String) {
        logger.debug { "CONFIGURATION: Creando directorios en caso de no existir" }

        directorios.forEach {
            val dir = java.io.File(it)
            logger.debug { "Creando directorio con ruta: $it" }
            Files.createDirectories(dir.toPath())
        }
    }
}