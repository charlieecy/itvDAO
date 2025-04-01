package org.example.models

import org.lighthousegames.logging.logging
import java.time.LocalDate
import java.time.LocalDateTime

data class Vehiculo (
    val id: Long = _newId,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val motor: Motor,
    val fechaMatriculacion: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    private val logger = logging()

    companion object{  // para que, por defecto, el nuevo id sea -1 en cualquier vehículo que se crea, ya que lo delegamos en la base de datos
        val _newId = -1L
    }

    enum class Motor {
        HIBRIDO, ELECTRICO, COMBUSTION
    }

    override fun toString(): String {
        logger.debug { "MODEL: Imprimiendo vehículo" }
        return "Vehiculo (id = $id, matrícula = $matricula, marca = $marca, modelo = $modelo, motor = $motor, fechaMatriculacion = $fechaMatriculacion, createdAt = $createdAt, updatedAt = $updatedAt)"
    }
}