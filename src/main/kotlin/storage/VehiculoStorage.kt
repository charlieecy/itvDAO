package org.example.storage

import org.example.models.Vehiculo
import java.io.File

interface VehiculoStorage {
    fun fileRead(file: File): List<Vehiculo>
    fun fileWrite(concesionario: List<Vehiculo>, file: File)
}
