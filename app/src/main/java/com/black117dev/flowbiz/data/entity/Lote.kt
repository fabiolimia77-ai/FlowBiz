package com.black117dev.flowbiz.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lotes",
    foreignKeys = [
        ForeignKey(
            entity = Linea::class,
            parentColumns = ["id"],
            childColumns = ["lineaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Proveedor::class,
            parentColumns = ["id"],
            childColumns = ["proveedorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("lineaId"),
        Index("proveedorId")
    ]
)
data class Lote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lineaId: Long,                 // ¿En qué línea está este lote?
    val cantidad: Double,              // Ej: 100.0 unidades
    val costoUnitario: Double,         // Ej: 4.50 USD
    val fechaCompra: Long,             // timestamp en milisegundos
    val proveedorId: Long              // quién lo vendió
)