package org.example.service

import com.github.benmanes.caffeine.cache.Cache
import org.example.exceptions.Exceptions
import org.example.models.Vehiculo
import org.example.repository.VehiculoRepository
import org.example.storage.VehiculoStorage
import org.example.validator.Validator
import org.lighthousegames.logging.logging
import java.io.File

class VehiculoServiceImpl(
    private val repository: VehiculoRepository,
    private val cache: Cache<Long, Vehiculo>,
    private val validator: Validator<Vehiculo>,
    private val storage: VehiculoStorage
): VehiculoService {
    private val logger = logging()

    override fun findAllPaginated(page: Int, size: Int): List<Vehiculo> {
        logger.debug { "SERVICE: Encontrando todos los vehículos paginados" }

        return repository.findAllPaginated(page, size)
    }

    override fun findByMatricula(matricula: String): Vehiculo {
        logger.debug { "SERVICE: Buscando vehículo por matrícula" }

        return repository.findByMatricula(matricula) ?:
        throw Exceptions.NotFoundException("Vehículo con matricula $matricula no encontrado")
    }

    override fun getAll(): List<Vehiculo> {
        logger.debug { "SERVICE: Obteniendo todos los vehículos" }

        return repository.getAll()
    }

    override fun getdById(id: Long): Vehiculo {
        logger.debug { "SERVICE: Buscando vehículo por id" }

        //Buscamos en la caché
        var vehiculoBuscado = cache.getIfPresent(id)
        //Si está, lo devolvemos
        if (vehiculoBuscado != null){
            return vehiculoBuscado
        }


        //Si no, buscamos en el repositorio
        vehiculoBuscado = repository.getById(id)
        //Si está, lo guardamos en la caché y lo devolvemos
        if (vehiculoBuscado != null){
            cache.put(id, vehiculoBuscado)
            return vehiculoBuscado
        }

        //Si tampoco está, lanzamos la excepción
        throw Exceptions.NotFoundException("Vehículo con id $id no encontrado")
    }

    override fun save(vehiculo: Vehiculo): Vehiculo {
        logger.debug { "SERVICE Guardando vehículo:" }

        validator.validate(vehiculo)

        return repository.save(vehiculo)
    }

    override fun delete(id: Long): Vehiculo {
        logger.debug { "SERVICE: Eliminando vehículo con id $id" }

        //Intentamos eliminarlo del repositorio
        val vehiculoEliminado = repository.delete(id)

        //Si lo eliminamos, lo borramos también de la caché y lo devolvemos
        if (vehiculoEliminado != null){
            cache.invalidate(id)
            return vehiculoEliminado
        }

        //Si no, lanzamos la excepción
        throw  Exceptions.NotFoundException("No se ha eliminado el vehículo con id $id, al no encontrarlo")
    }

    override fun update(id: Long, vehiculo: Vehiculo): Vehiculo {
        logger.debug { "SERVICE: Actualizando vehículo con id $id" }

        return repository.update(id,vehiculo)?:
        throw Exceptions.NotFoundException("No se ha actualizado el vehículo con id $id, al no encontrarlo")
    }

    override fun readFromFile(file: File): List<Vehiculo> {
        return storage.fileRead(file)
    }

    override fun writeToFile(file: File) {
        storage.fileWrite(repository.getAll(), file)
    }
}