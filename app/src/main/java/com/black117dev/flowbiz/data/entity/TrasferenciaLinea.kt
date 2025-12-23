// data/entity/TransferenciaLinea.kt
package com.black117dev.flowbiz.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transferencias_linea")
data class TransferenciaLinea(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val origenId: Long,
    val destinoId: Long,
    val cantidad: Double,
    val comision: Double, // siempre 5.0
    val fecha: Long
)