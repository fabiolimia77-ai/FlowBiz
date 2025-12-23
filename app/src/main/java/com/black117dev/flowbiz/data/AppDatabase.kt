package com.black117dev.flowbiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.black117dev.flowbiz.data.dao.LineaDao
import com.black117dev.flowbiz.data.dao.ProveedorDao
import com.black117dev.flowbiz.data.dao.LoteDao
import com.black117dev.flowbiz.data.dao.VentaDao
import com.black117dev.flowbiz.data.dao.TransferenciaLineaDao
import com.black117dev.flowbiz.data.entity.Linea
import com.black117dev.flowbiz.data.entity.Proveedor
import com.black117dev.flowbiz.data.entity.Lote
import com.black117dev.flowbiz.data.entity.Venta
import com.black117dev.flowbiz.data.entity.TransferenciaLinea

@Database(
    entities = [
        Linea::class,
        Proveedor::class,
        Lote::class,
        Venta::class,
        TransferenciaLinea::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun lineaDao(): LineaDao
    abstract fun proveedorDao(): ProveedorDao
    abstract fun loteDao(): LoteDao
    abstract fun ventaDao(): VentaDao
    abstract fun transferenciaLineaDao(): TransferenciaLineaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flowbiz_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}