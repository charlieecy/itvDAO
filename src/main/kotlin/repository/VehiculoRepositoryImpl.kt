package org.example.repository

import org.example.dao.VehiculosDAO
import org.example.mapper.toEntity
import org.example.mapper.toModel
import org.example.models.Vehiculo
import org.lighthousegames.logging.logging
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class VehiculoRepositoryImpl(
    private val dao: VehiculosDAO
): VehiculoRepository {
    private val logger = logging()

    override fun findAllPaginated(page: Int, size: Int): List<Vehiculo> {
        logger.debug { "REPOSITORY: Buscando todos los vehículos paginados" }

        return dao.findAllPaginated(page, page * size).map { it.toModel() }
    }

    override fun findByMatricula(matricula: String): Vehiculo? {
        logger.debug { "REPOSITORY: Buscando por matrícula: $matricula" }

        return dao.findByMatricula(matricula)?.toModel()
    }

    override fun getAll(): List<Vehiculo> {
        logger.debug { "REPOSITORY: Encontrando todos los vehículos" }

        return dao.getAll().map { it.toModel() }
    }

    override fun getById(id: Long): Vehiculo? {
        logger.debug { "REPOSITORY: Buscando por id: $id" }

        return dao.getById(id)?.toModel()
    }

    override fun save(item: Vehiculo): Vehiculo {
        logger.debug { "REPOSITORY: Guardando vehículo" }

        //Actualizamos los campos createdAt y updatedAt
        val timeStamp = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS) //Truncated para que no dé priblemas con el formato del mapper
        val vehiculoToSave = item.copy(createdAt = timeStamp, updatedAt = timeStamp).toEntity()

        //Guardamos el vehículo en la base de datos y lo devolvemos como modelo
        val idGenerated = dao.save(vehiculoToSave).toLong()
        val savedVehiculo = dao.getById(idGenerated)

        logger.info { "Vehículo guardado con éxito" }
        return savedVehiculo!!.toModel()
    }

    override fun delete(id: Long): Vehiculo? {
        logger.debug { "REPOSITORY: Eliminando vehículo con id: $id" }

        val deletedVehiculo: Vehiculo? = dao.getById(id)?.toModel()

        if (dao.delete(id) == 0) {
            logger.info { "No se ha podido eliminar el vehículo con id $id porque no existe" }
        } else {
            logger.info { "Vehiculo con id $id eliminado con éxito" }
        }

        return deletedVehiculo
    }

    override fun update(id: Long, item: Vehiculo): Vehiculo? {
        logger.debug { "REPOSITORY: Actualizando vehículo con id: $id" }

        //Buscamos si existe en la bbdd
        val vehiculoToUpdate: Vehiculo? = dao.getById(id)?.toModel()

        if (vehiculoToUpdate != null) {
            //Actualizamos el campo updatedAt y el id
            val timeStamp = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)  //Le damos formato de 6 cifras decimales para que no salte excepción en el mapper

            val updatedVehiculo = vehiculoToUpdate.copy(id = id, updatedAt = timeStamp)

            //Lo intentamos guardar actualizado
            if (dao.update(updatedVehiculo.toEntity()) == 0){ //Si no se ha podido actualizar, devuelve 0 líneas afectadas
                logger.info { "El vehículo no ha podido actualizarse" }
                return null
            } else {
                return updatedVehiculo
            }
        }
        return null
    }
}