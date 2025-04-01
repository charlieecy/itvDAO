package org.example.validator

import org.example.exceptions.Exceptions
import org.example.models.Vehiculo
import org.lighthousegames.logging.logging
import java.time.LocalDate

class VehiculoValidator: Validator<Vehiculo> {
    private val logger = logging()

    override fun validate (vehiculo: Vehiculo): Vehiculo {
        logger.debug { "VALIDATOR: Validando vehículo" }

        val regEx = """^\d{4}[BCDFGHJKLMNPRSTVWXYZ]{3}$""".toRegex()

        if (vehiculo.matricula.isBlank()) throw Exceptions.ValidationException("La matrícula no puede estar vacía")

        if (!vehiculo.matricula.matches(regEx)) throw Exceptions.ValidationException("La matrícula no tiene un formato correcto (NNNNLLL)")

        if (vehiculo.marca.isBlank()) throw Exceptions.ValidationException("La marca no puede estar vacía")

        if (vehiculo.modelo.isBlank()) throw Exceptions.ValidationException("El modelo no puede estar vacío")

        if (vehiculo.fechaMatriculacion > LocalDate.now()) throw Exceptions.ValidationException("La fecha de matriculación no puede ser posterior a la fecha actual")

        return vehiculo
    }
}