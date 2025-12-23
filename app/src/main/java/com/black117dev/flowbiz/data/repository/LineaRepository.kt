package com.black117dev.flowbiz.data.repository

import com.black117dev.flowbiz.data.dao.LineaDao
import com.black117dev.flowbiz.data.entity.Linea

class LineaRepository(private val lineaDao: LineaDao) {

    suspend fun insert(linea: Linea) = lineaDao.insert(linea)
    suspend fun update(linea: Linea) = lineaDao.update(linea)
    suspend fun delete(linea: Linea) = lineaDao.delete(linea)
    suspend fun getAll() = lineaDao.getAll()
    suspend fun getById(id: Long) = lineaDao.getById(id)
}