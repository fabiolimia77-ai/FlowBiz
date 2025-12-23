package com.black117dev.flowbiz.data.dao

import androidx.room.*
import com.black117dev.flowbiz.data.entity.Lote

@Dao
interface LoteDao {

    @Insert
    suspend fun insert(lote: Lote): Long

    @Update
    suspend fun update(lote: Lote)

    @Delete
    suspend fun delete(lote: Lote)

    /**
     * Obtiene todos los lotes de una línea, ordenados del más antiguo al más reciente.
     * ¡ES ESENCIAL PARA FIFO!
     */
    @Query("SELECT * FROM lotes WHERE lineaId = :lineaId ORDER BY fechaCompra ASC")
    suspend fun getLotesByLineaOrdered(lineaId: Long): List<Lote>

    /**
     * Calcula el stock total de una línea.
     */
    @Query("SELECT SUM(cantidad) FROM lotes WHERE lineaId = :lineaId")
    suspend fun getTotalStock(lineaId: Long): Double?

    /**
     * Elimina un lote por ID (cuando se consume completamente).
     */
    @Query("DELETE FROM lotes WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * Verifica si una línea tiene algún lote (para no permitir borrar líneas con stock).
     */
    @Query("SELECT COUNT(*) > 0 FROM lotes WHERE lineaId = :lineaId")
    suspend fun hasLotes(lineaId: Long): Boolean
}