package com.black117dev.flowbiz.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "lineas",
    indices = [Index(value = ["numero"], unique = true)]
)
data class Linea(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val numero: String
)