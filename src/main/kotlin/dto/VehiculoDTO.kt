package org.example.dto

import org.example.models.Vehiculo.Companion._newId
import org.example.models.Vehiculo.Motor
import java.time.LocalDate
import java.time.LocalDateTime

data class VehiculoDTO (
    val matricula: String,
    val marca: String,
    val modelo: String,
    val motor: String,
    val fechaMatriculacion: String,
) {
}