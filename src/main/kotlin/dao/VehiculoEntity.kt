package org.example.dao

import java.time.LocalDate
import java.time.LocalDateTime

class VehiculoEntity (
    val id: Long,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val motor: String,
    val fechaMatriculacion: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
){
}