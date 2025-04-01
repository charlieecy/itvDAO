package org.example.repository

import org.example.models.Vehiculo

interface VehiculoRepository: CRUDRepository <Vehiculo, Long> {
    fun findAllPaginated (page: Int = 1, size: Int = 10): List<Vehiculo>
    fun findByMatricula (matricula: String): Vehiculo?
}