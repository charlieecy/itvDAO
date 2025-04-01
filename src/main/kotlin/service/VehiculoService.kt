package org.example.service

import org.example.models.Vehiculo
import java.io.File

interface VehiculoService {
    fun findAllPaginated (page: Int, size: Int): List<Vehiculo>
    fun findByMatricula (matricula: String): Vehiculo
    fun getAll(): List<Vehiculo>
    fun getdById(id: Long): Vehiculo
    fun save(vehiculo: Vehiculo): Vehiculo
    fun delete(id: Long): Vehiculo
    fun update(id: Long, vehiculo: Vehiculo): Vehiculo
    fun readFromFile(file: File): List<Vehiculo>
    fun writeToFile(file: File)

}