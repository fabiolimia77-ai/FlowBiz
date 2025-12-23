// data/repository/VentaRepository.kt
package com.black117dev.flowbiz.data.repository

import com.black117dev.flowbiz.data.dao.VentaDao
import com.black117dev.flowbiz.data.entity.Venta

class VentaRepository(private val ventaDao: VentaDao) {
    suspend fun insert(venta: Venta) = ventaDao.insert(venta)
}