package org.example.di

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.example.config.Config
import org.example.dao.VehiculosDAO
import org.example.jdbi.JdbiManager
import org.example.models.Vehiculo
import org.example.repository.VehiculoRepository
import org.example.repository.VehiculoRepositoryImpl
import org.example.service.VehiculoService
import org.example.service.VehiculoServiceImpl
import org.example.storage.VehiculoStorage
import org.example.storage.VehiculoStorageImpl
import org.example.validator.Validator
import org.example.validator.VehiculoValidator
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging
import java.util.concurrent.TimeUnit

object Dependencies {
    private val logger = logging()

    init {
        logger.debug { "Inicializando gestor de dependencias" }
    }

    private fun provideDatabaseManager(): Jdbi {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando JDBI" }
        return JdbiManager.instance
    }

    private fun provideVehiculosDao(jdbi: Jdbi): VehiculosDAO {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando DAO de Vehiculos" }
        return jdbi.onDemand(VehiculosDAO::class.java)
    }


    private fun provideVehiculosCache(
        capacity: Long = Config.cacheSize,
        duration: Long = Config.cacheExpiration
    ): Cache<Long, Vehiculo> {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Caché de Vehículos (capacidad: $capacity - duración: $duration)" }
        return Caffeine.newBuilder()
            .maximumSize(capacity) // LRU con máximo de 5 elementos
            .expireAfterWrite(duration, TimeUnit.SECONDS) // Expira x segundos después de la escritura
            .build<Long, Vehiculo>()
    }

    private fun provideVehiculosRepository(dao: VehiculosDAO): VehiculoRepository {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Repositorio de Vehículos" }
        return VehiculoRepositoryImpl(dao)
    }


    private fun provideVehiculosValidator(): Validator<Vehiculo> {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Validador de Vehículos" }
        return VehiculoValidator()
    }

    private fun provideVehiculosStorage(): VehiculoStorage {
        logger.debug { "INYECCIÓN DE DEPENDENCIAS: Proporcionando Storage de Vehículos" }
        return VehiculoStorageImpl()
    }


    private fun provideVehiculosService(
        repository: VehiculoRepository,
        cache: Cache<Long, Vehiculo>,
        validator: Validator<Vehiculo>,
        storage: VehiculoStorage
    ): VehiculoService {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Servicio de Vehículos" }
        return VehiculoServiceImpl(repository, cache, validator, storage)
    }

    fun getVehiculosService(): VehiculoService {
        return provideVehiculosService(
            repository = provideVehiculosRepository(provideVehiculosDao(provideDatabaseManager())),
            cache = provideVehiculosCache(),
            validator = provideVehiculosValidator(),
            storage = provideVehiculosStorage()
        )
    }
}