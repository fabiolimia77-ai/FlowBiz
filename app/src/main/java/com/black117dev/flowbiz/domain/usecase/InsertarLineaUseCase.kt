package com.black117dev.flowbiz.domain.usecase

import com.black117dev.flowbiz.data.repository.LineaRepository
import com.black117dev.flowbiz.data.entity.Linea

class InsertarLineaUseCase(
    private val lineaRepository: LineaRepository
) {
    suspend operator fun invoke(nombre: String, numero: String) {
        require(nombre.isNotBlank()) { "Nombre no puede estar vacío" }
        require(numero.isNotBlank()) { "Número no puede estar vacío" }

        val linea = Linea(
            nombre = nombre,
            numero = numero
        )
        lineaRepository.insert(linea)
    }
}