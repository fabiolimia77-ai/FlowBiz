package com.black117dev.flowbiz.data.dao

import androidx.room.*
import com.black117dev.flowbiz.data.entity.Proveedor

@Dao
interface ProveedorDao {

    @Insert
    suspend fun insert(proveedor: Proveedor): Long

    @Update
    suspend fun update(proveedor: Proveedor)

    @Delete
    suspend fun delete(proveedor: Proveedor)

    @Query("SELECT * FROM proveedores")
    suspend fun getAll(): List<Proveedor>

    @Query("SELECT * FROM proveedores WHERE id = :id")
    suspend fun getById(id: Long): Proveedor?

    @Query("SELECT * FROM proveedores WHERE nombre LIKE :query")
    suspend fun searchByName(query: String): List<Proveedor>
}