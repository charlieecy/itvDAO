package repository

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.example.dao.VehiculosDAO
import org.example.mapper.toEntity
import org.example.models.Vehiculo
import org.example.repository.VehiculoRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
@DisplayName("Repositorio")
class VehiculoRepositoryImplTest {

    @MockK
    private lateinit var dao: VehiculosDAO

    @InjectMockKs
    private lateinit var repository: VehiculoRepositoryImpl

    private val vehiculo = Vehiculo(
        matricula = "5192HFF",
        marca = "Ford",
        modelo = "Kuga",
        motor = Vehiculo.Motor.COMBUSTION,
        fechaMatriculacion = LocalDate.of(2014,10,31),
    )

    @Nested
    @DisplayName("Test correctos")
    inner class TestCorrectos {

        @Test
        @DisplayName("Buscar por matrícula")
        fun buscarPorMatricula() {
            //Given
            every { dao.findByMatricula("5192HFF") } returns vehiculo.toEntity()

            //When
            val result = repository.findByMatricula("5192HFF")

            //Then
            assertAll(
                {assertEquals(vehiculo.id, result!!.id)},
                {assertEquals(vehiculo.matricula, result!!.matricula)}
            )

            verify (exactly = 1) {dao.findByMatricula("5192HFF")}
        }

        @Test
        @DisplayName("Buscar por ID")
        fun buscarPorId() {
            //Given
            every { dao.getById(1L) } returns vehiculo.toEntity()

            //When
            val result = repository.getById(1)

            //Then
            assertAll(
                {assertEquals(vehiculo.id, result!!.id)},
                {assertEquals(vehiculo.matricula, result!!.matricula)}
            )

            verify (exactly = 1) {dao.getById(1L)}
        }

        @Test
        @DisplayName("Buscar todos los vehículos")
        fun buscarTodosLosVehiculos() {
            //Given
            every { dao.getAll() } returns listOf(vehiculo.toEntity())

            //When
            val result = repository.getAll()

            //Then
            assertAll(
                {assertEquals(1, result.size)},
                {assertEquals(vehiculo.id, result[0].id)},
                {assertEquals(vehiculo.matricula, result[0].matricula)}
            )

            verify (exactly = 1) {dao.getAll()}
        }

        @Test
        @DisplayName("Guardar vehículo")
        fun guardarVehiculo() {
            //Given
            every { dao.save(vehiculo.toEntity()) } returns 1
            every { dao.getById(1) } returns vehiculo.copy(id= 1L).toEntity()

            //When
            val result = repository.save(vehiculo)

            //Then
            assertAll(
                {assertEquals(vehiculo.id, result.id)},
                {assertEquals(vehiculo.matricula, result.matricula)}
            )

            verify (exactly = 1) {dao.save(vehiculo.toEntity())}
            verify (exactly = 1) {dao.getById(vehiculo.id)}
            //verify (exactly = 1) {Vehiculo.copy}
        }

        @Test
        @DisplayName("Borrar vehículo")
        fun borrarVehiculo() {
            //Given
            every { dao.getById(1) } returns vehiculo.toEntity()
            every { dao.delete(1) } returns 1

            //When
            val result = repository.delete(1)

            //Then
            assertAll(
                {assertEquals(vehiculo.id, result!!.id)},
                {assertEquals(vehiculo.matricula, result!!.matricula)},
                {assertEquals(vehiculo.marca, result!!.marca)},
                {assertEquals(vehiculo.modelo, result!!.modelo)},
                {assertEquals(vehiculo.motor, result!!.motor)},
                {assertEquals(vehiculo.fechaMatriculacion, result!!.fechaMatriculacion)},
            )

            verify (exactly = 1) {dao.getById(1)}
            verify (exactly = 1) {dao.delete(1)}
        }



    }
}