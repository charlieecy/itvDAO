package validator

import org.example.exceptions.Exceptions
import org.example.models.Vehiculo
import org.example.validator.VehiculoValidator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("Validador")
class VehiculoValidatorTest{

    private var validator = VehiculoValidator()

    @Nested
    @DisplayName("Test correctos")
    inner class TestCorrectos{

        @Test
        @DisplayName("Datos correctos")
        fun testValidarVehiculoDatosCorrectos() {
            val vehiculo = Vehiculo(
                matricula = "1234HSF",
                marca = "Honda",
                modelo = "Civic",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.of(2000, 1, 1),
            )

            val res = validator.validate(vehiculo)

            assertAll(
                { assertDoesNotThrow { validator.validate(vehiculo) } },
                { assertEquals(res, vehiculo) }
            )
        }

    }

    @Nested
    @DisplayName("Test incorrectos")
    inner class TestInorrectos {

        @Test
        @DisplayName("Matrícula incorrecta")
        fun testValidarMatriculaIncorrecta() {
            val vehiculo = Vehiculo(
                matricula = "1234AAA",
                marca = "Honda",
                modelo = "Civic",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.of(2000, 1, 1),
            )

            val expected = "Error en la validación: La matrícula no tiene un formato correcto (NNNNLLL)"
            val actual = org.junit.jupiter.api.assertThrows<Exceptions.ValidationException> { validator.validate(vehiculo) }

            assertEquals(expected, actual.message)
        }

        @Test
        @DisplayName("Marca vacía")
        fun testValidarMarcaVacia() {
            val vehiculo = Vehiculo(
                matricula = "1234HSF",
                marca = "",
                modelo = "Civic",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.of(2000, 1, 1),
            )

            val expected = "Error en la validación: La marca no puede estar vacía"
            val actual = org.junit.jupiter.api.assertThrows<Exceptions.ValidationException> { validator.validate(vehiculo) }

            assertEquals(expected, actual.message)
        }

        @Test
        @DisplayName("Modelo vacío")
        fun testValidarModeloVacio() {
            val vehiculo = Vehiculo(
                matricula = "1234HSF",
                marca = "Honda",
                modelo = "",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.of(2000, 1, 1),
            )

            val expected = "Error en la validación: El modelo no puede estar vacío"
            val actual = org.junit.jupiter.api.assertThrows<Exceptions.ValidationException> { validator.validate(vehiculo) }

            assertEquals(expected, actual.message)
        }

        @Test
        @DisplayName("Fecha de matriculación futura")
        fun testValidarFechaMatriculacionFutura() {

            val vehiculo = Vehiculo(
                matricula = "1234HSF",
                marca = "Honda",
                modelo = "Civic",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.now().plusDays(1),
            )

            val expected = "Error en la validación: La fecha de matriculación no puede ser posterior a la fecha actual"
            val actual = org.junit.jupiter.api.assertThrows<Exceptions.ValidationException> { validator.validate(vehiculo) }

            assertEquals(expected, actual.message)

        }

    }

}
