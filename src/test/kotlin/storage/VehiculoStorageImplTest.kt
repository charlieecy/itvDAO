package storage

import org.example.exceptions.Exceptions
import org.example.models.Vehiculo
import org.example.storage.VehiculoStorageImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.time.LocalDate

@DisplayName("Storage")
class VehiculoStorageImplTest {
    private val storage = VehiculoStorageImpl()
    @Nested
    @DisplayName("Test correctos")
    inner class TestsCorrectos {

        @Test
        @DisplayName("Lectura de fichero")
        fun lecturaFichero(@TempDir tempDir: File) {

            //Preparamos el fichero
            val file = File(tempDir, "data.csv")
            val fileContent = """
                MATRICULA,MARCA,MODELO,MOTOR,FECHAMATRICULACION
                3456JKL,Hyundai,Kona,ELECTRICO,18/09/2020
            """.trimIndent()
            file.writeText(fileContent)

            val expected = listOf(
                Vehiculo(
                    matricula = "3456JKL",
                    marca = "Hyundai",
                    modelo = "Kona",
                    motor = Vehiculo.Motor.ELECTRICO,
                    fechaMatriculacion = LocalDate.of(2020, 9, 18)
                )
            )

            val result = storage.fileRead(file)

            assertAll(
                { assertEquals(result.size, 1) },
                { assertEquals(result.size, expected.size) },
                { assertEquals(result[0].id, expected[0].id) },
                { assertEquals(result[0].matricula, expected[0].matricula) },
                { assertEquals(result[0].marca, expected[0].marca) },
                { assertEquals(result[0].modelo, expected[0].modelo) },
                { assertEquals(result[0].motor, expected[0].motor) },
                { assertEquals(result[0].fechaMatriculacion, expected[0].fechaMatriculacion) }
            )

        }

        @Test
        @DisplayName ("Escritura de fichero")
        fun escrituraFichero(@TempDir tempDir: File) {

            val file = File(tempDir, "backup.csv")

            val concesionario = listOf(
                Vehiculo(
                    matricula = "3456JKL",
                    marca = "Hyundai",
                    modelo = "Kona",
                    motor = Vehiculo.Motor.ELECTRICO,
                    fechaMatriculacion = LocalDate.of(2020, 9, 18)
                )
            )

            storage.fileWrite(concesionario, file)

            val expected = """
                MATRICULA,MARCA,MODELO,MOTOR,FECHAMATRICULACION
                3456JKL,Hyundai,Kona,ELECTRICO,2020-09-18
            """.trimIndent()

            val actual = file.readText().trimIndent()

            assertEquals(expected, actual)
        }
    }
    @Nested
    @DisplayName("Test incorrectos")
    inner class TestsIncorrectos {

        @Test
        @DisplayName("Leer fichero inexistente")
        fun ficheroInexistente() {

            val file = File("esta/ruta/no/existe.csv")

            val expected = "Error en el almacenamiento: El fichero no existe, la ruta especificada no es un fichero o no se tienen permisos de lectura"
            val actual = org.junit.jupiter.api.assertThrows<Exceptions.StorageException> { storage.fileRead(file) }

            assertEquals(expected, actual.message)
        }

        @Test
        @DisplayName("Leer algo que no es un fichero")
        fun noEsFichero() {

            val file = File("esto/no/es/un/fichero")

            val expected = "Error en el almacenamiento: El fichero no existe, la ruta especificada no es un fichero o no se tienen permisos de lectura"
            val actual = org.junit.jupiter.api.assertThrows<Exceptions.StorageException> { storage.fileRead(file) }

            assertEquals(expected, actual.message)
        }

        @Test
        @DisplayName("Intentar escribir sin que el directorio padre exista")
        fun parentFileDoesNotExists() {
            val file = File("noexisto/backup.csv")
            val concesionario = listOf(
                Vehiculo(
                    matricula = "3456JKL",
                    marca = "Hyundai",
                    modelo = "Kona",
                    motor = Vehiculo.Motor.ELECTRICO,
                    fechaMatriculacion = LocalDate.of(2020, 9, 18)
                )
            )

            val expected = "Error en el almacenamiento: El directorio padre del fichero no existe"
            val actual = org.junit.jupiter.api.assertThrows<Exceptions.StorageException> { storage.fileWrite(concesionario,file) }

            assertEquals(expected, actual.message)
        }
    }
}