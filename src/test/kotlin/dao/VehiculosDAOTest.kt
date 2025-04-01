package dao
import org.example.dao.VehiculosDAO
import org.example.di.Dependencies.provideDatabaseManager
import org.example.di.Dependencies.provideVehiculosDao
import org.example.mapper.toEntity
import org.example.models.Vehiculo
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNull
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehiculosDAOTest {
    private lateinit var dao: VehiculosDAO

    @BeforeEach
    fun setUp() {
        val jdbi = provideDatabaseManager()
        dao = provideVehiculosDao(jdbi)

        //Creamos la tabla
        var inputStream = ClassLoader.getSystemResourceAsStream("tables.sql")?.bufferedReader()!!
        var script = inputStream.readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }

        //Cargamos los datos de prueba
        inputStream = ClassLoader.getSystemResourceAsStream("data.sql")?.bufferedReader()!!
        script = inputStream.readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }

    @Nested
    @DisplayName("Test correctos")
    inner class TestCorrectos {

        @Test
        @DisplayName("Buscar paginado")
        fun buscarPaginado() {
            val res = dao.findAllPaginated(1,6)

            assertAll(
                {assertEquals(res.size, 1)},
                {assertEquals(res[0].id, 7L)},
                {assertEquals(res[0].matricula, "6789QWR")}
            )
        }


        @Test
        @DisplayName("Buscar por matrícula")
        fun buscarPorMatricula() {
            val res = dao.findByMatricula("1234HSF")

            assertAll(
                {assertEquals(res!!.id, 1L)},
                {assertEquals(res!!.matricula, "1234HSF")},
                {assertEquals(res!!.marca, "Honda")},
                {assertEquals(res!!.modelo, "Civic")},
                {assertEquals(res!!.motor, "COMBUSTION")},
                {assertEquals(res!!.fechaMatriculacion, LocalDate.parse("2000-01-01"))},
            )
        }

        @Test
        @DisplayName("Buscar todos")
        fun getAll() {
            val res = dao.getAll()

            assertAll(
                {assertEquals(res.size, 10)},
                {assertEquals(res[0].id, 1L)},
                {assertEquals(res[0].matricula, "1234HSF")},
                {assertEquals(res[9].id, 10L)},
                {assertEquals(res[9].matricula, "1122BSD")}
            )
        }

        @Test
        @DisplayName("Buscar por ID")
        fun getById() {
            val res = dao.getById(7)

            assertAll(
                {assertEquals(res!!.id, 7L)},
                {assertEquals(res!!.matricula, "6789QWR")},
                {assertEquals(res!!.marca, "Renault")},
                {assertEquals(res!!.modelo, "Megane")},
                {assertEquals(res!!.motor, "COMBUSTION")},
                {assertEquals(res!!.fechaMatriculacion, LocalDate.parse("2017-03-18"))},
            )
        }

        @Test
        @DisplayName("Guardar vehículo")
        fun guardarVehiculo() {
            val vehiculo = Vehiculo(
                matricula = "5192HFF",
                marca = "Ford",
                modelo = "Kuga",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.of(2014,10,31),
            ).toEntity()

            val result = dao.save(vehiculo)

            assertEquals(11, result, "El ID del vehículo guardado debe ser 11")
        }

        @Test
        @DisplayName("Borrar vehículo")
        fun borrarVehiculo() {
            val res = dao.delete(1)
            assertEquals(1, res, "Debe devolver 1, al haberse visto afectada una fila de la tabla")
        }

        @Test
        @DisplayName("Actualizar vehículo")
        fun actualizarVehiculo() {

            val vehiculo = Vehiculo(
                id = 1L,
                matricula = "1234HSF",
                marca = "Honda",
                modelo = "Civic",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.of(2000,1,1),
            ).toEntity()

            val res = dao.update(vehiculo)

            assertEquals(1, res, "Debe devolver 1, al haberse visto afectada una fila de la tabla")
        }


    }


    @Nested
    @DisplayName("Test incorrectos")
    inner class TestIncorrectos {
        @Test
        @DisplayName("Buscar por matrícula inexistente")
        fun buscarPorMatriculaInexistente() {
            val res = dao.findByMatricula("1234567")
            assertNull(res)
        }

        @Test
        @DisplayName("Buscar paginado con índice negativo")
        fun buscarPaginadoNegativo() {
            assertThrows<UnableToExecuteStatementException> { dao.findAllPaginated(-1,6) }
        }

        @Test
        @DisplayName("Buscar todos return incorrecto")
        fun getAll() {
            val res = dao.getAll()

            assertAll(
                {assertNotEquals(res.size, 0)},
                {assertNotEquals(res[0].id, 7L)},
                {assertNotEquals(res[0].matricula, "sdfsdsdg")},
                {assertNotEquals(res[9].id, 14L)},
                {assertNotEquals(res[9].matricula, "dsfsdfdsf")}
            )
        }

        @Test
        @DisplayName("Buscar por ID inexistente")
        fun getByIdInexistente() {
            val res = dao.getById(15)
            assertNull(res)
        }

        @Test
        @DisplayName("Borrar vehículo inexistente")
        fun borrarVehiculoInexistente() {
            val res = dao.delete(15)
            assertEquals(0, res, "Debe devolver 0, al haberse visto afectadas 0 filas de la tabla")
        }

        @Test
        @DisplayName("Actualizar vehículo inexistente")
        fun actualizarVehiculoInexistente() {

            val vehiculo = Vehiculo(
                id = 17L,
                matricula = "1234HSF",
                marca = "Honda",
                modelo = "Civic",
                motor = Vehiculo.Motor.COMBUSTION,
                fechaMatriculacion = LocalDate.of(2000,1,1),
            ).toEntity()

            val res = dao.update(vehiculo)

            assertEquals(0, res, "Debe devolver 0, al haberse visto afectadas 0 filas de la tabla")
        }
    }






}