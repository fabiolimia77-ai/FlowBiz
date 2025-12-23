// data/dao/VentaDao.kt
package com.black117dev.flowbiz.data.dao

import androidx.room.*
import com.black117dev.flowbiz.data.entity.Venta

@Dao
interface VentaDao {
    @Insert
    suspend fun insert(venta: Venta): Long

    @Query("SELECT * FROM ventas")
    suspend fun getAll(): List<Venta>
}