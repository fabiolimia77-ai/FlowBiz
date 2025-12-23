package com.black117dev.flowbiz.data.dao

import androidx.room.*
import com.black117dev.flowbiz.data.entity.Linea

@Dao
interface LineaDao {

    @Insert
    suspend fun insert(linea: Linea): Long

    @Update
    suspend fun update(linea: Linea)

    @Delete
    suspend fun delete(linea: Linea)

    @Query("SELECT * FROM lineas")
    suspend fun getAll(): List<Linea>

    @Query("SELECT * FROM lineas WHERE id = :id")
    suspend fun getById(id: Long): Linea?

    /**
     * Verifica si una línea tiene nombre o número duplicado (opcional, si usas índices)
     */
    @Query("SELECT COUNT(*) FROM lineas WHERE numero = :numero")
    suspend fun countByNumero(numero: String): Int
}