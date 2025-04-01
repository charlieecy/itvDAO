package org.example
import org.example.config.Config
import org.example.di.Dependencies
import org.example.models.Vehiculo
import java.io.File
import java.time.LocalDate

fun main() {
    val service = Dependencies.getVehiculosService()

    //Obtenemos todos los que hay por defecto (a partir de data.sql)
    service.getAll().forEach { println(it) }

    //Búsqueda paginada
    println(service.findAllPaginated(5,0))

    //Buscamos por id (existe)
    println(service.getdById(1))

    //Buscamos por id (no existe)
    try {
        println(service.getdById(14))
    } catch (e: Exception) {
        println(e.message)
    }

    //Buscamos por matrícula (existe)
    println(service.findByMatricula("7890XYZ"))

    //Buscamos por matrícula (no existe)
    try {
        println(service.findByMatricula("aaaj2"))
    } catch (e: Exception) {
        println(e.message)
    }

    //Guardamos un vehículo (datos correctos)
    val vehiculo = Vehiculo(
        matricula = "5192HFF",
        marca = "Ford",
        modelo = "Kuga",
        motor = Vehiculo.Motor.COMBUSTION,
        fechaMatriculacion = LocalDate.of(2014,10,31),
    )
    println(service.save(vehiculo))

    //Guardamos un vehículo (datos incorrectos)
    val vehiculo2 = Vehiculo(
        matricula = "5192AEI",
        marca = "Ford",
        modelo = "Kuga",
        motor = Vehiculo.Motor.COMBUSTION,
        fechaMatriculacion = LocalDate.of(2014,10,31),
    )
    try {
        println(service.save(vehiculo2))
    } catch (e: Exception) {
        println(e.message)
    }

    //Eliminamos un vehículo (existe)
    println(service.delete(4))
    println("Vehículos en la Base de datos tras la eliminación:")
    service.getAll().forEach { println(it) }

    //Eliminamos un vehículo (no existe)
    try {
        println(service.delete(22))
    } catch (e: Exception) {
        println(e.message)
    }

    //Actualizamos un vehículo (existe)
    println(service.update(11, vehiculo))

    //Actualizamos un vehículo (no existe)
    try {
        println(service.update(22, vehiculo))
    } catch (e: Exception) {
        println(e.message)
    }

    //Leemos el fichero csv para importar vehículos
    val file: File = File(Config.storageData, "concesionario.csv")

    try {
        service.readFromFile(file).forEach { service.save(it) }
    } catch (e: Exception) {
        println(e.message)
    }
    service.getAll().forEach { println(it) }

    //Escribimos en un fichero
    val file2: File = File(Config.storageOutput, "concesionarioBackup.csv")
    service.writeToFile(file2)
}