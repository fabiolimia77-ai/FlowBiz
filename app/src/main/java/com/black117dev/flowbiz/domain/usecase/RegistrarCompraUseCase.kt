package com.black117dev.flowbiz.domain.usecase

import com.black117dev.flowbiz.data.repository.LoteRepository
import com.black117dev.flowbiz.data.entity.Lote
import java.lang.IllegalArgumentException

/**
 * Registra una compra creando un nuevo lote en la base de datos.
 *
 * Reglas de negocio:
 * - La cantidad debe ser > 0
 * - El costo total debe ser > 0
 * - El costo unitario se calcula como costoTotal / cantidad
 */
class RegistrarCompraUseCase(
    private val loteRepository: LoteRepository
) {
    suspend operator fun invoke(
        lineaId: Long,
        cantidad: Double,
        costoTotal: Double,
        proveedorId: Long,
        fechaCompra: Long = System.currentTimeMillis()
    ) {
        // Validaciones de negocio
        require(cantidad > 0) { "La cantidad debe ser mayor que cero" }
        require(costoTotal > 0) { "El costo total debe ser mayor que cero" }
        require(lineaId > 0) { "ID de línea inválido" }
        require(proveedorId > 0) { "ID de proveedor inválido" }

        val costoUnitario = costoTotal / cantidad

        val lote = Lote(
            lineaId = lineaId,
            cantidad = cantidad,
            costoUnitario = costoUnitario,
            fechaCompra = fechaCompra,
            proveedorId = proveedorId
        )

        loteRepository.insert(lote)
    }
}