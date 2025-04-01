package org.example.mapper

import org.example.dao.VehiculoEntity
import org.example.dto.VehiculoDTO
import org.example.models.Vehiculo
import org.lighthousegames.logging.logging
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val logger = logging()

fun VehiculoEntity.toModel(): Vehiculo {
    logger.debug { "MAPPER: Mapeando entidad a modelo" }

    return Vehiculo(
        id = this.id,
        matricula = this.matricula,
        marca = this.marca,
        modelo = this.modelo,
        motor = Vehiculo.Motor.valueOf(this.motor),
        fechaMatriculacion = this.fechaMatriculacion,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Vehiculo.toEntity(): VehiculoEntity {
    logger.debug { "MAPPER: Mapeando modelo a entidad" }

    return VehiculoEntity(
        id = this.id,
        matricula = this.matricula,
        marca = this.marca,
        modelo = this.modelo,
        motor = this.motor.toString(),
        fechaMatriculacion = this.fechaMatriculacion,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun VehiculoDTO.toModel(): Vehiculo {
    logger.debug { "MAPPER: Mapeando DTO a modelo" }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    return Vehiculo(
        matricula = this.matricula,
        marca = this.marca,
        modelo = this.modelo,
        motor = Vehiculo.Motor.valueOf(this.motor),
        fechaMatriculacion = LocalDate.parse(this.fechaMatriculacion, formatter),
    )
}

fun Vehiculo.toDto(): VehiculoDTO {
    logger.debug { "MAPPER: Mapeando modelo a DTO" }

    return VehiculoDTO(
        matricula = this.matricula,
        marca = this.marca,
        modelo = this.modelo,
        motor = this.motor.toString(),
        fechaMatriculacion = this.fechaMatriculacion.toString(),
    )
}