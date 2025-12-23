// data/dao/TransferenciaLineaDao.kt
package com.black117dev.flowbiz.data.dao

import androidx.room.*
import com.black117dev.flowbiz.data.entity.TransferenciaLinea

@Dao
interface TransferenciaLineaDao {
    @Insert
    suspend fun insert(transferencia: TransferenciaLinea): Long
}