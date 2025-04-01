package org.example.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterKotlinMapper(VehiculoEntity::class)
interface VehiculosDAO {

    @SqlQuery("SELECT * FROM vehiculos LIMIT :limit OFFSET :offset")
    fun findAllPaginated(@Bind ("limit") limit: Int, @Bind("offset") offset: Int): List<VehiculoEntity>

    @SqlQuery ("SELECT * FROM vehiculos where matricula = :matricula")
    fun findByMatricula(@Bind("matricula") matricula: String): VehiculoEntity?

    @SqlQuery("SELECT * FROM vehiculos")
    fun getAll(): List<VehiculoEntity>

    @SqlQuery("SELECT * FROM vehiculos where id = :id")
    fun getById(@Bind ("id") id: Long): VehiculoEntity?

    @SqlUpdate("INSERT INTO vehiculos (matricula, marca, modelo, motor, fechaMatriculacion, createdAt, updatedAt) VALUES (:matricula, :marca, :modelo, :motor, :fechaMatriculacion, :createdAt, :updatedAt)")
    @GetGeneratedKeys("id") //Por que como el id es autonumérico y generado por la BBDD, lo necesitamos, es lo que devuelve la función
    fun save(@BindBean vehiculo: VehiculoEntity): Int

    @SqlUpdate("DELETE FROM vehiculos where id = :id")
    fun delete(@Bind("id") id: Long): Int // Devuelve el número de filas de la tabla afectadas. Si devuelve 1, se ha eliminado el vehículo. Si devuelve 0, no porque no existe ningún vehículo con ese id.

    @SqlUpdate("UPDATE vehiculos SET matricula = :matricula, marca = :marca, modelo = :modelo, motor = :motor, fechaMatriculacion = :fechaMatriculacion, createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id")
    fun update(@BindBean vehiculo: VehiculoEntity): Int // Devuelve el número de filas de la tabla afectadas. Si devuelve 1, se ha actualizado el vehículo. Si devuelve 0, no porque no existe ningún vehículo con ese id.
}