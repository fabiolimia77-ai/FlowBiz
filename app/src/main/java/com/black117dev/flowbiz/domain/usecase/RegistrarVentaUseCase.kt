package com.black117dev.flowbiz.domain.usecase

import com.black117dev.flowbiz.data.entity.Lote
import com.black117dev.flowbiz.data.entity.Venta
import com.black117dev.flowbiz.data.repository.LoteRepository
import com.black117dev.flowbiz.data.repository.VentaRepository
import kotlin.math.min

/**
 * Registra una venta aplicando:
 * - FIFO para determinar el costo real
 * - Descuento fijo de 5 unidades monetarias
 * - Validación de stock suficiente
 */
class RegistrarVentaUseCase(
    private val loteRepository: LoteRepository,
    private val ventaRepository: VentaRepository
) {

    suspend operator fun invoke(
        cliente: String,
        cantidadVendida: Double,
        precioUnitario: Double,
        lineaId: Long,
        comisionPorcentual: Double? = null,
        modoCalculo: String = "DESPUES_DESCUENTO", // o "ANTES_DESCUENTO"
        tipo: String = "EFECTIVO" // o "PENDIENTE"
    ) {
        require(cliente.isNotBlank()) { "El cliente no puede estar vacío" }
        require(cantidadVendida > 0) { "La cantidad vendida debe ser > 0" }
        require(precioUnitario > 0) { "El precio unitario debe ser > 0" }
        require(lineaId > 0) { "ID de línea inválido" }

        // 1. Validar stock suficiente
        val stockDisponible = loteRepository.getTotalStock(lineaId) ?: 0.0
        require(cantidadVendida <= stockDisponible) {
            "Stock insuficiente. Disponible: $stockDisponible, solicitado: $cantidadVendida"
        }

        // 2. Aplicar descuento fijo de 5
        val montoBruto = precioUnitario * cantidadVendida
        val montoConDescuento = montoBruto - 5.0 // ✅ Descuento fijo obligatorio

        // 3. Aplicar comisión (opcional)
        val montoFinal = comisionPorcentual?.let { porcentaje ->
            when (modoCalculo) {
                "ANTES_DESCUENTO" -> montoBruto * (1 - porcentaje / 100)
                else -> montoConDescuento * (1 - porcentaje / 100) // por defecto
            }
        } ?: montoConDescuento

        // 4. Aplicar FIFO: consumir lotes y calcular costo total real
        val costoTotalVendido = aplicarFifoConsumo(lineaId, cantidadVendida)

        // 5. Registrar la venta
        val venta = Venta(
            cliente = cliente,
            cantidadVendida = cantidadVendida,
            montoBruto = montoBruto, // guardamos el bruto original
            comisionPorcentual = comisionPorcentual,
            modoCalculo = modoCalculo,
            tipo = tipo,
            fecha = System.currentTimeMillis()
        )
        ventaRepository.insert(venta)

        // Nota: En Fase 2, aquí registraríamos la ganancia y transferencias pendientes.
        // Por ahora, el FIFO ya actualizó el inventario.
    }

    /**
     * Consume la cantidad especificada de lotes usando FIFO.
     * Actualiza o elimina los lotes afectados.
     * Devuelve el costo total real de lo vendido.
     */
    private suspend fun aplicarFifoConsumo(lineaId: Long, cantidadAConsumir: Double): Double {
        var cantidadRestante = cantidadAConsumir
        var costoTotal = 0.0

        // Obtener lotes ordenados del más antiguo al más reciente
        val lotes = loteRepository.getLotesByLineaOrdered(lineaId)

        for (lote in lotes) {
            if (cantidadRestante <= 0) break

            val cantidadDisponible = lote.cantidad
            val cantidadAExtraer = min(cantidadRestante, cantidadDisponible)

            // Acumular costo
            costoTotal += cantidadAExtraer * lote.costoUnitario

            // Actualizar o eliminar lote
            if (cantidadAExtraer == cantidadDisponible) {
                // Lote se agota → eliminarlo
                loteRepository.delete(lote)
            } else {
                // Lote parcialmente consumido → actualizar
                val loteActualizado = lote.copy(cantidad = cantidadDisponible - cantidadAExtraer)
                loteRepository.update(loteActualizado)
            }

            cantidadRestante -= cantidadAExtraer
        }

        return costoTotal
    }
}