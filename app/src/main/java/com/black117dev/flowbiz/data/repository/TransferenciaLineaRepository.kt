// data/repository/TransferenciaLineaRepository.kt
package com.black117dev.flowbiz.data.repository

import com.black117dev.flowbiz.data.dao.TransferenciaLineaDao
import com.black117dev.flowbiz.data.entity.TransferenciaLinea

class TransferenciaLineaRepository(
    private val dao: TransferenciaLineaDao
) {
    suspend fun insert(transferencia: TransferenciaLinea) = dao.insert(transferencia)
}