// data/entity/Venta.kt
package com.black117dev.flowbiz.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ventas")
data class Venta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cliente: String,
    val cantidadVendida: Double,
    val montoBruto: Double,
    val comisionPorcentual: Double?,
    val modoCalculo: String,
    val tipo: String, // "EFECTIVO" o "PENDIENTE"
    val fecha: Long
)