package com.black117dev.flowbiz.domain.usecase

import com.black117dev.flowbiz.data.entity.Lote
import com.black117dev.flowbiz.data.entity.TransferenciaLinea
import com.black117dev.flowbiz.data.repository.LoteRepository
import com.black117dev.flowbiz.data.repository.TransferenciaLineaRepository
import kotlin.math.min

/**
 * Transfiere producto de una línea a otra, aplicando:
 * - Comisión fija de 5 unidades monetarias (registrada, no descontada del producto)
 * - FIFO para mover los lotes más antiguos
 * - Preservación del costo unitario original
 */
class TransferirEntreLineasUseCase(
    private val loteRepository: LoteRepository,
    private val transferenciaRepo: TransferenciaLineaRepository
) {

    suspend operator fun invoke(
        origenId: Long,
        destinoId: Long,
        cantidad: Double
    ) {
        // Validaciones básicas
        require(origenId > 0) { "ID de origen inválido" }
        require(destinoId > 0) { "ID de destino inválido" }
        require(cantidad > 0) { "La cantidad a transferir debe ser > 0" }
        require(origenId != destinoId) { "Origen y destino no pueden ser la misma línea" }

        // Validar stock suficiente en origen
        val stockOrigen = loteRepository.getTotalStock(origenId) ?: 0.0
        require(cantidad <= stockOrigen) {
            "Stock insuficiente en línea de origen. Disponible: $stockOrigen"
        }

        // Aplicar FIFO en origen: consumir lotes y crear nuevos en destino
        transferirConFifo(origenId, destinoId, cantidad)

        // Registrar la transferencia (con comisión fija de 5)
        val transferencia = TransferenciaLinea(
            origenId = origenId,
            destinoId = destinoId,
            cantidad = cantidad,
            comision = 5.0, // ✅ Comisión fija obligatoria
            fecha = System.currentTimeMillis()
        )
        transferenciaRepo.insert(transferencia)
    }

    /**
     * Mueve 'cantidad' de producto del origen al destino usando FIFO.
     * Crea nuevos lotes en el destino con los mismos datos (costo, fecha, proveedor).
     */
    private suspend fun transferirConFifo(origenId: Long, destinoId: Long, cantidadAMover: Double) {
        var cantidadRestante = cantidadAMover
        val lotesOrigen = loteRepository.getLotesByLineaOrdered(origenId)

        for (loteOrigen in lotesOrigen) {
            if (cantidadRestante <= 0) break

            val cantidadDisponible = loteOrigen.cantidad
            val cantidadAMoverDeEsteLote = min(cantidadRestante, cantidadDisponible)

            // Crear nuevo lote en el DESTINO (mismos datos, nueva línea)
            val nuevoLoteDestino = Lote(
                id = 0, // Room generará ID
                lineaId = destinoId,
                cantidad = cantidadAMoverDeEsteLote,
                costoUnitario = loteOrigen.costoUnitario,
                fechaCompra = loteOrigen.fechaCompra,
                proveedorId = loteOrigen.proveedorId
            )
            loteRepository.insert(nuevoLoteDestino)

            // Actualizar o eliminar lote en ORIGEN
            if (cantidadAMoverDeEsteLote == cantidadDisponible) {
                loteRepository.delete(loteOrigen)
            } else {
                val loteActualizado = loteOrigen.copy(
                    cantidad = cantidadDisponible - cantidadAMoverDeEsteLote
                )
                loteRepository.update(loteActualizado)
            }

            cantidadRestante -= cantidadAMoverDeEsteLote
        }
    }
}