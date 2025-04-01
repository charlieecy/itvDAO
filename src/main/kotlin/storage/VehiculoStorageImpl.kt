package org.example.storage

import org.example.dto.VehiculoDTO
import org.example.exceptions.Exceptions
import org.example.mapper.toDto
import org.example.mapper.toModel
import org.example.models.Vehiculo
import org.lighthousegames.logging.logging
import java.io.File

class VehiculoStorageImpl: VehiculoStorage {
    private val logger = logging()

    override fun fileRead(file: File): List<Vehiculo> {
        logger.debug { "STORAGE: Leyendo fichero ${file.absolutePath}" }

        if (!file.exists() || !file.isFile || !file.canRead()) throw Exceptions.StorageException("El fichero no existe, la ruta especificada no es un fichero o no se tienen permisos de lectura")

        return file.readLines()
            .drop(1)
            .map{it.split(",")}
            .map{
                VehiculoDTO(
                    matricula = it[0],
                    marca = it[1],
                    modelo = it[2],
                    motor = it[3],
                    fechaMatriculacion = it[4]
                ).toModel()
            }
    }

    override fun fileWrite(concesionario: List<Vehiculo>, file: File) {
        logger.debug { "STORAGE: Escribiendo en fichero ${file.absolutePath}" }

        if (!file.parentFile.exists() || !file.parentFile.isDirectory) {
            throw Exceptions.StorageException("El directorio padre del fichero no existe")
        }

        file.writeText("MATRICULA,MARCA,MODELO,MOTOR,FECHAMATRICULACION\n")

        concesionario.map {
            it.toDto()
            file.appendText("${it.matricula},${it.marca},${it.modelo},${it.motor},${it.fechaMatriculacion}\n")
        }
    }


}