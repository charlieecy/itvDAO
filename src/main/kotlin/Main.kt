package org.example
import org.example.config.Config
import org.example.di.Dependencies
import org.example.models.Vehiculo
import java.io.File
import java.time.LocalDate

fun main() {
    val service = Dependencies.getVehiculosService()

    println()
    println("----------------------------------")
    println()

    //Obtenemos todos los que hay por defecto (a partir de data.sql)
    service.getAll().forEach { println(it) }

    println()
    println("----------------------------------")
    println()

    //Búsqueda paginada
    println(service.findAllPaginated(5,0))

    println()
    println("----------------------------------")
    println()

    //Buscamos por id (existe)
    println(service.getdById(1))

    println()
    println("----------------------------------")
    println()

    //Buscamos por id (no existe)
    try {
        println(service.getdById(14))
    } catch (e: Exception) {
        println(e.message)
    }

    println()
    println("----------------------------------")
    println()

    //Buscamos por matrícula (existe)
    println(service.findByMatricula("7890XYZ"))

    println()
    println("----------------------------------")
    println()

    //Buscamos por matrícula (no existe)
    try {
        println(service.findByMatricula("aaaj2"))
    } catch (e: Exception) {
        println(e.message)
    }

    println()
    println("----------------------------------")
    println()

    //Guardamos un vehículo (datos correctos)
    val vehiculo = Vehiculo(
        matricula = "5192HFF",
        marca = "Ford",
        modelo = "Kuga",
        motor = Vehiculo.Motor.COMBUSTION,
        fechaMatriculacion = LocalDate.of(2014,10,31),
    )
    println(service.save(vehiculo))

    println()
    println("----------------------------------")
    println()

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

    println()
    println("----------------------------------")
    println()

    //Eliminamos un vehículo (existe)
    println(service.delete(4))
    println("Vehículos en la Base de datos tras la eliminación:")
    service.getAll().forEach { println(it) }

    println()
    println("----------------------------------")
    println()

    //Eliminamos un vehículo (no existe)
    try {
        println(service.delete(22))
    } catch (e: Exception) {
        println(e.message)
    }

    println()
    println("----------------------------------")
    println()

    //Actualizamos un vehículo (existe)
    println(service.update(11, vehiculo))

    println()
    println("----------------------------------")
    println()

    //Actualizamos un vehículo (no existe)
    try {
        println(service.update(22, vehiculo))
    } catch (e: Exception) {
        println(e.message)
    }

    println()
    println("----------------------------------")
    println()

    //Leemos el fichero csv para importar vehículos
    val file: File = File(Config.storageData, "concesionario.csv")

    try {
        service.readFromFile(file).forEach {
            try {
                service.save(it)
            } catch (e: Exception) {
                println("${vehiculo.matricula} ${e.message}")
            }
        }
    } catch (e: Exception) {
        println(e.message)
    }
    service.getAll().forEach { println(it) }

    println()
    println("----------------------------------")
    println()

    //Escribimos en un fichero
    val file2: File = File(Config.storageOutput, "concesionarioBackup.csv")
    service.writeToFile(file2)
}