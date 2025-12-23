package com.black117dev.flowbiz.data.repository

import com.black117dev.flowbiz.data.dao.LoteDao
import com.black117dev.flowbiz.data.entity.Lote

class LoteRepository(private val loteDao: LoteDao) {

    suspend fun insert(lote: Lote) = loteDao.insert(lote)
    suspend fun update(lote: Lote) = loteDao.update(lote)
    suspend fun delete(lote: Lote) = loteDao.delete(lote)
    suspend fun getLotesByLineaOrdered(lineaId: Long) = loteDao.getLotesByLineaOrdered(lineaId)
    suspend fun getTotalStock(lineaId: Long) = loteDao.getTotalStock(lineaId)
    suspend fun hasLotes(lineaId: Long) = loteDao.hasLotes(lineaId)
}